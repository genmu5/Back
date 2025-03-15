package com.example.BEF.Course.Service;

import com.example.BEF.Course.DTO.*;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.Saved;
import com.example.BEF.Course.Domain.UserCourse;
import com.example.BEF.Course.Repository.CourseDisabilityRepository;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Course.Repository.SavedRepository;
import com.example.BEF.Course.Repository.UserCourseRepository;
import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Repository.DisabledRepository;
import com.example.BEF.Location.DTO.LocationInfoRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.User.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final CourseDisabilityRepository courseDisabilityRepository;
    private final LocationRepository locationRepository;
    private final DisabledRepository disabledRepository;
    private final SavedRepository savedRepository;
    private final AIRecCourse aiRecCourse;

    @Transactional
    // 코스 생성
    public CourseInfoRes addCourse(User user, CreateCourseReq createCourseReq) {
        Course course = addCourseInfo(user, createCourseReq);

        return new CourseInfoRes(course.getCourseNumber(), course.getCourseName());
        return new CourseLocationRes(course, courseDisabilityRepository.findAllByCourse(course), locationInfoResList);
    }

    private Course addCourseInfo(User user, CreateCourseReq createCourseReq) {
        Course course = courseRepository.findCourseByCourseNumber(createCourseReq.getCourseNumber());

        course.setCourseName(createCourseReq.getCourseName());
        course.setStartDate(createCourseReq.getStartDate());
        course.setEndDate(createCourseReq.getStartDate().plusDays(course.getPeriod()));
        course.setUser(user);

        return courseRepository.save(course);
    }

    // 코스 삭제
    public CourseInfoRes deleteUserCourse(Course course) {
        // 해당 코스 내에 속한 모든 유저 코스 정보 삭제
        List<UserCourse> userCourseList = userCourseRepository.findUserCoursesByCourse(course);
        userCourseRepository.deleteAll(userCourseList);

        // 코스 정보 삭제
        courseRepository.delete(course);

        return CourseInfoRes.of(course, courseDisabilityRepository.findAllByCourse(course));
    }

    // 코스 장소 삭제
    public void delLocToCourse(Course course, Location location) {
        // 유저 코스 정보 생성
        UserCourse userCourse = userCourseRepository.findUserCourseByCourseAndLocation(course, location);
        userCourseRepository.delete(userCourse);
    }

    // 코스 목록 조회
    public List<CourseInfoRes> findUserCourses(User user) {
        return courseRepository.findCourseNumbersAndCourseNamesByUser(user);
    }

    // 관광지 저장
    public CourseSaveRes saveLocation(User user, Location location) {
        // 유저 코스 정보 생성
        Saved saved = Saved.of(user, location);
        savedRepository.save(saved);

        // 유저 코스 응답 리턴
        return (new CourseSaveRes(location.getContentId()));
    }

    public void deleteSavedLocation(User user, Location location) {
        // 특정 유저가 저장한 특정 관광지 조회
        Optional<Saved> saved = savedRepository.findByUserAndLocation(user, location);

        if (saved.isPresent()) {
            savedRepository.delete(saved.get());
        } else {
            throw new IllegalArgumentException("해당 관광지가 저장되어 있지 않습니다.");
        }
    }

    // 저장한 관광지 목록 조회
    public List<LocationInfoRes> getUserSaveLocations(User user) {

        // 유저가 저장한 관광지들
        List<Saved> savedList = savedRepository.findAllByUser(user);

        // 저장한 관광지 정보 리스트
        List<LocationInfoRes> locationInfoResList = new ArrayList<>();

        for (Saved saved : savedList) {
            Location location = saved.getLocation();
            LocationInfoRes locationInfoRes = new LocationInfoRes(location, disabledRepository.findDisabledByLocation(location));
            locationInfoResList.add(locationInfoRes);
        }

        return (locationInfoResList);
    }

    // 코스 관광지 목록 조회
    public CourseLocationRes getCourseLocationList(Course course) {

        // 저장한 관광지 정보 리스트
        List<UserCourse> userCourses = userCourseRepository.findUserCoursesByCourse(course);

        List<LocationInfoRes> locationInfoResList = new ArrayList<>();

        for (UserCourse userCourse : userCourses) {
            Location location = userCourse.getLocation();
            Disabled disabled = disabledRepository.findDisabledByLocation(location);
            LocationInfoRes locationInfoRes = new LocationInfoRes(location, disabled);
            locationInfoResList.add(locationInfoRes);
        }

        return (new CourseLocationRes(course, courseDisabilityRepository.findAllByCourse(course), locationInfoResList));
    }

    @Transactional
    public CourseLocRes createAIRecCourse(Long area, Long period, List<Long> disability, List<Long> tripType) {

        // 필터링 된 관광지
        List<Location> filteredLocation = locationRepository.filterByAreaAndDisabilityAndTravelType(
                        area, disability, tripType).stream()
                .limit(80)
                .toList();

//        // 필터링 된 음식점
//        List<Location> filteredRestaurant = locationRepository.filterByAreaAndContentType(area).stream()
//                .limit(80)
//                .toList();

        return aiRecCourse.generateCourse(filteredLocation, disability, tripType, area, period);
    }

    @Transactional
    public CourseLocRes updateCourse(Course course, List<List<Long>> courseLocByDay) {
        updateCourseOrder(courseLocByDay, course);

        return CourseLocRes.of(course.getCourseNumber(), course.getCourseName(), createSimpleLocs(courseLocByDay));
    }

    private List<List<SimpleLoc>> createSimpleLocs(List<List<Long>> courseLocByDay) {
        return courseLocByDay.stream()
                .map(list -> list.stream()
                        .map(contentId -> SimpleLoc.from(locationRepository.findLocationByContentId(contentId)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private void updateCourseOrder(List<List<Long>> courseLocByDay, Course course) {
        Long day = 1L;
        for (List<Long> courseByDay : courseLocByDay) {
            Long order = 1L;
            for (Long locationId : courseByDay) {
                updateUserCourse(course, locationId, day, order);
                order++;
            }
            day++;
        }
    }

    private void updateUserCourse(Course course, Long locationId, Long day, Long order) {
        UserCourse userCourse = userCourseRepository.findUserCourseByCourseAndLocation(course, locationRepository.findLocationByContentId(locationId));
        userCourse.setDay(day);
        userCourse.setOrder(order);

        userCourseRepository.save(userCourse);
    }
}
