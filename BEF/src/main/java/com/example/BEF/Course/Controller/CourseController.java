package com.example.BEF.Course.Controller;

import com.example.BEF.Course.DTO.CourseInfoRes;
import com.example.BEF.Course.DTO.CourseLocRes;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Service.CourseRepository;
import com.example.BEF.Course.Service.CourseService;
import com.example.BEF.Location.DTO.UserLocationRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Service.LocationRepository;
import com.example.BEF.User.Domain.User;
import com.example.BEF.User.Service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/course")
@Controller
public class CourseController {
    @Autowired
    CourseService courseService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CourseRepository courseRepository;

    // 코스 리스트 생성 API
    @PostMapping("/{userNumber}/create")
    public ResponseEntity<CourseInfoRes> createCourse(@PathVariable("userNumber") Long userNumber, @RequestParam("name") String name) {
        // 유저 조회
        User user = userRepository.findUserByUserNumber(userNumber);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스 생성
        return ResponseEntity.status(HttpStatus.OK).body(courseService.createUserCourse(user, name));
    }

    // 코스 리스트 삭제 API
    @DeleteMapping("/{userNumber}/delete")
    public ResponseEntity<CourseInfoRes> deleteCourse(@PathVariable("userNumber") Long userNumber, @RequestParam("courseNumber") Long courseNumber) {
        // 유저 및 코스 조회
        User user = userRepository.findUserByUserNumber(userNumber);
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);

        // 존재하지 않는 유저일 때
        if (user == null || course == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스 생성
        return ResponseEntity.status(HttpStatus.OK).body(courseService.deleteUserCourse(course));
    }

    // 코스 장소 추가 API
    @PostMapping("/{courseNumber}/add/{contentId}")
    public ResponseEntity<CourseLocRes> addLocation(@PathVariable("courseNumber") Long courseNumber, @PathVariable("contentId") Long contentId) {
        // 코스 및 관광지 조회
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);
        Location location = locationRepository.findLocationByContentId(contentId);

        // 존재하지 않는 코스 및 관광지일 때
        if (course == null || location == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.addLocToCourse(course, location));
    }

    // 코스 장소 삭제 API
    @DeleteMapping("/{courseNumber}/delete/{contentId}")
    public ResponseEntity<CourseLocRes> delLocation(@PathVariable("courseNumber") Long courseNumber, @PathVariable("contentId") Long contentId) {
        // 코스 및 관광지 조회
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);
        Location location = locationRepository.findLocationByContentId(contentId);

        // 존재하지 않는 코스 및 관광지일 때
        if (course == null || location == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.delLocToCourse(course, location));
    }

    // 나의 코스 목록 조회 API
    @GetMapping("/{userNumber}")
    public ResponseEntity<List<CourseInfoRes>> getCourses(@PathVariable("userNumber") Long userNumber) {
        // 유저 조회
        User user = userRepository.findUserByUserNumber(userNumber);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.findUserCourses(user));
    }

    // 관광지 저장 API
    @PostMapping("/save")
    public ResponseEntity<CourseLocRes> saveLocation(@RequestParam("userNumber") Long userNumber, @RequestParam("contentId") Long contentId) {
        // 유저 및 관광지 조회
        User user = userRepository.findUserByUserNumber(userNumber);
        Location location = locationRepository.findLocationByContentId(contentId);

        // 존재하지 않는 관광지일 때
        if (location == null || user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.saveLocation(user, location));
    }

    // 저장한 관광지 조회 API
    @GetMapping("/save/{userNumber}")
    public ResponseEntity<List<UserLocationRes>> saveLocationList(@PathVariable("userNumber") Long userNumber) {
        // 유저 조회
        User user = userRepository.findUserByUserNumber(userNumber);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.getUserSaveLocations(user));
    }

    // 코스 관광지 조회 API
    @GetMapping("/{courseNumber}/list")
    public ResponseEntity<List<UserLocationRes>> CourseLocationList(@PathVariable("courseNumber") Long courseNumber) {
        // 유저 조회
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);

        // 존재하지 않는 유저일 때
        if (course == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseLocationList(course));
    }
}
