package com.example.BEF.Course.Service;

import com.example.BEF.Course.DTO.*;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.UserCourse;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Course.Repository.UserCourseRepository;
import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Repository.DisabledRepository;
import com.example.BEF.Location.DTO.LocationInfoRes;
import com.example.BEF.Location.DTO.UserLocationRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.User.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final LocationRepository locationRepository;
    private final DisabledRepository disabledRepository;

    // 코스 생성
    public CourseInfoRes createUserCourse(User user, CreateCourseReq createCourseReq) {
        Course course = new Course(user, createCourseReq.getName(), createCourseReq.getDescription());
        courseRepository.save(course);

        return new CourseInfoRes(course.getCourseNumber(), course.getCourseName(), course.getDescription());
    }

    // 코스 삭제
    public CourseInfoRes deleteUserCourse(Course course) {
        // 해당 코스 내에 속한 모든 유저 코스 정보 삭제
        List<UserCourse> userCourseList = userCourseRepository.findUserCoursesByCourse(course);
        userCourseRepository.deleteAll(userCourseList);

        // 코스 정보 삭제
        courseRepository.delete(course);

        return new CourseInfoRes(course.getCourseNumber(), course.getCourseName(), course.getDescription());
    }

    // 코스 장소 추가
    public CourseLocRes addLocToCourse(Course course, List<Long> contentIdList) {

        for (Long contentId : contentIdList) {
            Location location = locationRepository.findLocationByContentId(contentId);

            // 유저 코스 정보 생성
            UserCourse userCourse = new UserCourse(course, location);
            userCourseRepository.save(userCourse);
        }

        // 유저 코스 응답 리턴
        return (new CourseLocRes(course.getCourseNumber(), course.getCourseName(), contentIdList));
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
        // 유저가 저장한 관광지 코스
        Course saveLocations = courseRepository.findCourseByUserAndCourseName(user, "저장");

        // 유저 코스 정보 생성
        UserCourse userCourse = new UserCourse(saveLocations, location);
        userCourseRepository.save(userCourse);

        // 유저 코스 응답 리턴
        return (new CourseSaveRes(saveLocations.getCourseNumber(), saveLocations.getCourseName(), location.getContentId()));
    }

    // 저장한 관광지 목록 조회
    public List<UserLocationRes> getUserSaveLocations(User user) {

        // 유저가 저장한 관광지 코스
        Course saveLocations = courseRepository.findCourseByUserAndCourseName(user, "저장");

        // 저장한 관광지 정보 리스트
        List<UserCourse> userCourses = userCourseRepository.findUserCoursesByCourse(saveLocations);

        List<UserLocationRes> userLocationResList = new ArrayList<>();

        for (UserCourse userCourse : userCourses) {
            Location location = userCourse.getLocation();
            UserLocationRes userLocationRes = new UserLocationRes(location.getContentId(), location.getContentTitle(), location.getAddr(), location.getThumbnailImage());
            userLocationResList.add(userLocationRes);
        }

        return (userLocationResList);
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

        return (new CourseLocationRes(course, locationInfoResList));
    }
}
