package com.example.BEF.Course.Service;

import com.example.BEF.Area.Repository.AreaRepository;
import com.example.BEF.Course.DTO.ChatGPTRes;
import com.example.BEF.Course.DTO.CourseLocRes;
import com.example.BEF.Course.DTO.SimpleLoc;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.CourseDisability;
import com.example.BEF.Course.Domain.CourseTripType;
import com.example.BEF.Course.Domain.UserCourse;
import com.example.BEF.Course.Repository.CourseDisabilityRepository;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Course.Repository.CourseTripTypeRepository;
import com.example.BEF.Course.Repository.UserCourseRepository;
import com.example.BEF.Disability.DisabilityRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.TripType.TripTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIRecCourse {
    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.finetuning.model}")
    private String finetuningModel;

    private final RestTemplate restTemplate;
    private final AreaRepository areaRepository;
    private final CourseRepository courseRepository;
    private final DisabilityRepository disabilityRepository;
    private final TripTypeRepository tripTypeRepository;
    private final LocationRepository locationRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseDisabilityRepository courseDisabilityRepository;
    private final CourseTripTypeRepository courseTripTypeRepository;

    public CourseLocRes generateCourse(List<Location> locations, List<Long> disability, List<Long> tripType, Long area, Long period) {
        Map<String, Object> body = createOpenAIRequestBody(locations, area, period);
        HttpHeaders headers = createOpenAIRequestHeader();

        // OpenAI API 호출
        ResponseEntity<ChatGPTRes> response = restTemplate.postForEntity(
                openAiApiUrl, new HttpEntity<>(body, headers), ChatGPTRes.class);

        // 응답 처리
        try {
            String aiResponse = response.getBody().getChoice().getFirst().getMessage().getContent();
            log.info("AI Response Content: {}", aiResponse);

            Map<String, List<Long>> dayWiseContentIds = parseDayWiseContentIds(aiResponse);

            return saveAICourse(disability, tripType, period, area, dayWiseContentIds);
        }
        catch (NullPointerException e) {
            log.info("Null Pointer Exception 발생");
            return null;
        }
    }

    private void saveCourseDisabilities(List<Long> disabilities, Course course) {
        List<CourseDisability> courseDisabilities = new ArrayList<>();

        for (Long disability : disabilities) {
            courseDisabilities.add(CourseDisability.builder()
                    .course(course)
                    .disability(disabilityRepository.findDisabilityByDisabilityNumber(disability))
                    .build());
        }

        courseDisabilityRepository.saveAll(courseDisabilities);
    }

    private void saveCourseTripTypes(List<Long> tripTypes, Course course) {
        List<CourseTripType> courseTripTypes = new ArrayList<>();

        for (Long tripType : tripTypes) {
            courseTripTypes.add(CourseTripType.builder()
                    .course(course)
                    .tripType(tripTypeRepository.findTripTypeEntityByTripTypeNumber(tripType))
                    .build());
        }

        courseTripTypeRepository.saveAll(courseTripTypes);
    }

    private HttpHeaders createOpenAIRequestHeader() {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + openAiApiKey);

        return headers;
    }

    private Map<String, Object> createOpenAIRequestBody(List<Location> locations, Long area, Long period) {
        Map<String, Object> body = new HashMap<>();

        body.put("model", finetuningModel);
        body.put("messages", List.of(Map.of(
                "role", "user",
                "temperature", 0,
                "content", generatePrompt(locations, areaRepository.findByAreaCode(area).getAreaName(), period)
        )));

        return body;
    }

    private CourseLocRes saveAICourse(List<Long> disability, List<Long> tripType, Long period, Long area, Map<String, List<Long>> dayWiseContentIds) {

        Course course = Course.builder()
                .period(period)
                .area(areaRepository.findByAreaCode(area))
                .build();

        courseRepository.save(course);

        List<UserCourse> userCoursesByAICourse = new ArrayList<>();

        for (Long day = 1L; day <= 3; day++) {
            List<Long> locaionList = dayWiseContentIds.getOrDefault("Day " + day, List.of());
            Long courseOrder = 1L;

            for (Long content : locaionList) {
                UserCourse userCourse = new UserCourse(day, courseOrder, course, locationRepository.findLocationByContentId(content));
                if (userCourse.getLocation() == null)
                    continue;
                userCoursesByAICourse.add(userCourse);
                userCourseRepository.save(userCourse);
                courseOrder++;
            }
        }

        try {
            List<UserCourse> userCourseList = userCourseRepository.findUserCoursesByCourse(course);
            course.setGpsX(userCourseList.getFirst().getLocation().getGpsX());
            course.setGpsY(userCourseList.getFirst().getLocation().getGpsY());
        }
        catch (NullPointerException e) {
            log.info("Null Pointer Exception 발생");
            return null;
        }

        saveCourseDisabilities(disability, course);
        saveCourseTripTypes(tripType, course);

        return CourseLocRes.of(course.getCourseNumber(), course.getCourseName(), createSimpleLocsByCourse(userCoursesByAICourse));
    }

    private List<List<SimpleLoc>> createSimpleLocsByCourse(List<UserCourse> userCourses) {
        List<List<UserCourse>> groupedByDay = new ArrayList<>(
                userCourses.stream()
                        .collect(Collectors.groupingBy(UserCourse::getDay))
                        .values()
        );

        return groupedByDay.stream()
                .map(list -> list.stream()
                        .map(uc -> SimpleLoc.from(uc.getLocation()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private String generatePrompt(List<Location> locations, String area, Long period) {

        StringBuilder prompt = new StringBuilder();
        Long tripPeriod = period + 1L;
        String defaultPrompt = "I want to receive a travel course recommendation for " + tripPeriod + " days in " + area + ".";

        prompt.append(defaultPrompt);
        prompt.append("Use the Tourist and Restaurant lists I provide, which include gpsX and gpsY coordinates, to calculate travel distances and estimated travel times." +
                " For each day, include exactly 3 places with contentTypeId 12 (tourist attractions)." + //and 1 place with contentTypeId 39 (restaurants)." +
                " Ensure that the travel sequence minimizes travel time (preferably within 15–30 km between places) and provides a logical flow." +
                " Return the course in the following structure: 'Day 1: [contentId1, contentId2, contentId3]', where the content IDs represent the recommended places in order." +
                " Ensure the courses are evenly distributed across the days and maintain balance and variety in location selection." +
                " Provide an estimated travel time between each location in parentheses, using gpsX and gpsY coordinates.");

        // 개선 방안 : 프롬프트 캐싱, ptu 서비스 - 개인 단계에서 조금 어려움

        // Tourist 리스트 추가
        prompt.append("Tourist : [");
        prompt.append(locations.stream()
                .map(Location::toPrompt)
                .collect(Collectors.joining(", ")));
        prompt.append("]\n");

        // Restaurant 리스트 추가
//        prompt.append("Restaurants: [");
//        prompt.append(restaurants.stream()
//                .map(Location::toPrompt)
//                .collect(Collectors.joining(", ")));
//        prompt.append("]");

        log.info("Generated Prompt: {}", prompt);
        return prompt.toString();
    }

    private Map<String, List<Long>> parseDayWiseContentIds(String aiResponse) {
        Map<String, List<Long>> dayWiseContentIds = new LinkedHashMap<>();

        // 정규표현식으로 Day X와 해당 리스트 추출
        Pattern pattern = Pattern.compile("(Day \\d+): \\[(.*?)\\]");
        Matcher matcher = pattern.matcher(aiResponse);

        while (matcher.find()) {
            String day = matcher.group(1); // Day 이름 (예: Day 1)
            String[] ids = matcher.group(2).split(","); // contentId 리스트 추출
            List<Long> contentIds = Arrays.stream(ids)
                    .map(String::trim) // 공백 제거
                    .map(Long::parseLong) // Long 타입 변환
                    .collect(Collectors.toList()); // Collectors.toList() 사용
            dayWiseContentIds.put(day, contentIds);
        }

        return dayWiseContentIds;
    }
}
