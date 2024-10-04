package com.example.BEF.Course.Service;

import com.example.BEF.Course.DTO.CourseInfoRes;
import com.example.BEF.Course.DTO.CourseLocRes;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.UserCourse;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserCourseRepository userCourseRepository;

    // 코스 생성
    public CourseInfoRes createUserCourse(User user, String name) {
        Course course = new Course(user, name);
        courseRepository.save(course);

        return new CourseInfoRes(course.getCourseNumber(), course.getCourseName());
    }

    // 코스 삭제
    public CourseInfoRes deleteUserCourse(Course course) {
        // 해당 코스 내에 속한 모든 유저 코스 정보 삭제
        List<UserCourse> userCourseList = userCourseRepository.findUserCoursesByCourse(course);
        userCourseRepository.deleteAll(userCourseList);

        // 코스 정보 삭제
        courseRepository.delete(course);

        return new CourseInfoRes(course.getCourseNumber(), course.getCourseName());
    }

    // 코스 장소 추가
    public CourseLocRes addLocToCourse(Course course, Location location) {
        // 유저 코스 정보 생성
        UserCourse userCourse = new UserCourse(course, location);
        userCourseRepository.save(userCourse);

        // 유저 코스 응답 리턴
        return (new CourseLocRes(course.getCourseNumber(), course.getCourseName(), location.getContentId()));
    }

    // 코스 장소 삭제
    public CourseLocRes delLocToCourse(Course course, Location location) {
        // 유저 코스 정보 생성
        UserCourse userCourse = userCourseRepository.findUserCourseByCourseAndLocation(course, location);
        userCourseRepository.delete(userCourse);

        // 유저 코스 응답 리턴
        return (new CourseLocRes(course.getCourseNumber(), course.getCourseName(), location.getContentId()));
    }

    // 코스 목록 조회
    public List<CourseInfoRes> findUserCourses(User user) {
        return courseRepository.findCourseNumbersAndCourseNamesByUser(user);
    }
}
