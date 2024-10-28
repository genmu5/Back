package com.example.BEF.Course.Controller;

import com.example.BEF.Course.Service.CourseService;
import com.example.BEF.Course.DTO.*;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Location.DTO.UserLocationRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.User.Domain.User;
import com.example.BEF.User.Service.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/course")
@Controller
@RequiredArgsConstructor
@Tag(name = "Course", description = "코스 관련 API")
public class CourseController {

    private final CourseService courseService;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CourseRepository courseRepository;

    // 코스 리스트 생성 API
//    @PostMapping("/{userNumber}/create")
    @PostMapping("/{uuid}/create")
    @Operation(summary = "코스 리스트 생성", description = "코스 리스트 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 생성에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
//    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    @Parameter(name = "uuid", description = "uuid", example = "32")
//    public ResponseEntity<CourseInfoRes> createCourse(@PathVariable("userNumber") Long userNumber, @RequestBody CreateCourseReq createCourseReq) {
    public ResponseEntity<CourseInfoRes> createCourse(@PathVariable("uuid") String uuid, @RequestBody CreateCourseReq createCourseReq) {
        // 유저 조회
//        User user = userRepository.findUserByUserNumber(userNumber);
        User user = userRepository.findUserByUuid(uuid);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스 생성
        return ResponseEntity.status(HttpStatus.OK).body(courseService.createUserCourse(user, createCourseReq));
    }

    // 코스 리스트 삭제 API
//    @DeleteMapping("/{userNumber}/delete")
    @DeleteMapping("/{uuid}/delete")
    @Operation(summary = "코스 리스트 삭제", description = "코스 리스트 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 삭제에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
//    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    @Parameter(name = "uuid", description = "uuid", example = "32")
//    public ResponseEntity<CourseInfoRes> deleteCourse(@PathVariable("userNumber") Long userNumber, @RequestBody DeleteCourseReq deleteCourseReq) {
    public ResponseEntity<CourseInfoRes> deleteCourse(@PathVariable("uuid") String uuid, @RequestBody DeleteCourseReq deleteCourseReq) {
        // 유저 및 코스 조회
//        User user = userRepository.findUserByUserNumber(userNumber);
        User user = userRepository.findUserByUuid(uuid);
        Course course = courseRepository.findCourseByCourseNumber(deleteCourseReq.getCourseNumber());

        // 존재하지 않는 유저일 때
        if (user == null || course == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스 생성
        return ResponseEntity.status(HttpStatus.OK).body(courseService.deleteUserCourse(course));
    }

    // 코스 장소 추가 API
    @PostMapping("/{courseNumber}/add")
    @Operation(summary = "코스에 장소 추가", description = "코스 장소 추가 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스에 장소를 추가했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 코스 또는 관광지입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "courseNumber", description = "코스 번호", example = "25")
    public ResponseEntity<CourseLocRes> addLocation(@PathVariable("courseNumber") Long courseNumber, @RequestBody CourseAddLocReq courseAddLocReq) {
        // 코스 및 관광지 조회
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);

        // 존재하지 않는 코스 및 관광지일 때
        if (course == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.addLocToCourse(course, courseAddLocReq.getContentIdList()));
    }

    // 코스 장소 삭제 API - 수정 필요
    @DeleteMapping("/{courseNumber}/delete/{contentId}")
    @Operation(summary = "코스에 장소 삭제", description = "코스 장소 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스에 장소를 삭제했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 코스 또는 관광지입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "courseNumber", description = "코스 번호", example = "25"),
            @Parameter(name = "contentId", description = "관광지 번호", example = "125551")
    })
    public ResponseEntity<String> delLocation(@PathVariable("courseNumber") Long courseNumber, @PathVariable("contentId") Long contentId) {
        // 코스 및 관광지 조회
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);
        Location location = locationRepository.findLocationByContentId(contentId);

        // 존재하지 않는 코스 및 관광지일 때
        if (course == null || location == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스에서 관광지 삭제
        courseService.delLocToCourse(course, location);

        return ResponseEntity.status(HttpStatus.OK).body(course.getCourseName() + " 코스에서 " + location.getContentTitle() + " 관광지를 제거했습니다.");
    }

    // 나의 코스 목록 조회 API
//    @GetMapping("/{userNumber}")
    @GetMapping("/{uuid}")
    @Operation(summary = "나의 코스 목록 조회", description = "나의 코스 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 코스 목록을 조회했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
//    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    @Parameter(name = "uuid", description = "uuid", example = "32")
//    public ResponseEntity<List<CourseInfoRes>> getCourses(@PathVariable("userNumber") Long userNumber) {
    public ResponseEntity<List<CourseInfoRes>> getCourses(@PathVariable("uuid") String uuid) {
        // 유저 조회
//        User user = userRepository.findUserByUserNumber(userNumber);
        User user = userRepository.findUserByUuid(uuid);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.findUserCourses(user));
    }

    // 관광지 저장 API
    @PostMapping("/save")
    @Operation(summary = "관광지 저장", description = "관광지 저장 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관광지를 저장했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 관광지입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CourseSaveRes> saveLocation(@RequestBody SaveLocationReq saveLocationReq) {
        // 유저 및 관광지 조회
//        User user = userRepository.findUserByUserNumber(saveLocationReq.getUserNumber());
        User user = userRepository.findUserByUuid(saveLocationReq.getUuid());
        Location location = locationRepository.findLocationByContentId(saveLocationReq.getContentId());

        // 존재하지 않는 관광지일 때
        if (location == null || user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.saveLocation(user, location));
    }

    // 저장한 관광지 조회 API
//    @GetMapping("/save/{userNumber}")
    @GetMapping("/save/{uuid}")
    @Operation(summary = "저장한 관광지 조회", description = "저장한 관광지 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장한 관광지를 조회했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
//    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    @Parameter(name = "uuid", description = "uuid", example = "uuidTest")
//    public ResponseEntity<List<UserLocationRes>> saveLocationList(@PathVariable("userNumber") Long userNumber) {
    public ResponseEntity<List<UserLocationRes>> saveLocationList(@PathVariable("uuid") String uuid) {
        // 유저 조회
//        User user = userRepository.findUserByUserNumber(userNumber);
        User user = userRepository.findUserByUuid(uuid);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.getUserSaveLocations(user));
    }

    // 코스 관광지 조회 API
    @GetMapping("/{courseNumber}/list")
    @Operation(summary = "코스 관광지 조회", description = "코스 관광지 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 관광지를 조회했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "courseNumber", description = "코스 번호", example = "32")
    public ResponseEntity<CourseLocationRes> CourseLocationList(@PathVariable("courseNumber") Long courseNumber) {
        // 유저 조회
        Course course = courseRepository.findCourseByCourseNumber(courseNumber);

        // 존재하지 않는 유저일 때
        if (course == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.getCourseLocationList(course));
    }
}
