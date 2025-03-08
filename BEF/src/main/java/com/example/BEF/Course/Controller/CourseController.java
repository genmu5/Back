package com.example.BEF.Course.Controller;

import com.example.BEF.Course.Service.CourseService;
import com.example.BEF.Course.DTO.*;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Disability.Disability;
import com.example.BEF.Disability.UserDisability;
import com.example.BEF.Location.DTO.LocationInfoRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.TripType.TripType;
import com.example.BEF.TripType.UserTripType;
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

    // AI 코스 빠른 생성 API
    @PostMapping("/ai-rec-quick")
    public ResponseEntity<CourseLocRes> createAIRecQuick(@RequestBody AIQuickCourseReq aiCourseReq) {
        User user = userRepository.findUserByUserNumber(aiCourseReq.getUserNumber());
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        CourseLocRes courseLocRes = courseService.createAIRecCourse(aiCourseReq.getArea(), aiCourseReq.getPeriod(),
                user.getDisabilities().stream().map(UserDisability::getDisability).map(Disability::getDisabilityNumber).toList(),
                user.getTripTypes().stream().map(UserTripType::getTripType).map(TripType::getTripTypeNumber).toList());

        if (courseLocRes == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseLocRes);
    }

    // AI 코스 세부 생성 API
    @PostMapping("/ai-rec")
    public ResponseEntity<CourseLocRes> createAIRec(@RequestBody AICourseReq aiCourseReq) {
        CourseLocRes courseLocRes = courseService.createAIRecCourse(aiCourseReq.getArea(), aiCourseReq.getPeriod(), aiCourseReq.getDisability(), aiCourseReq.getTripType());

        if (courseLocRes == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(courseLocRes);
    }

    // 코스 추가 API
    @PostMapping("/{userNumber}/create")
    @Operation(summary = "코스 리스트 생성", description = "코스 리스트 생성 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 생성에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    public ResponseEntity<CourseInfoRes> addCourse(@PathVariable("userNumber") Long userNumber, @RequestBody CreateCourseReq createCourseReq) {
        // 유저 조회
        User user = userRepository.findUserByUserNumber(userNumber);

        // 존재하지 않는 유저일 때
        if (user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스 생성
        return ResponseEntity.status(HttpStatus.OK).body(courseService.addCourse(user, createCourseReq));
    }

    // 코스 삭제 API
    @DeleteMapping("/{userNumber}/delete")
    @Operation(summary = "코스 리스트 삭제", description = "코스 리스트 삭제 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스 삭제에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    public ResponseEntity<CourseInfoRes> deleteCourse(@PathVariable("userNumber") Long userNumber, @RequestBody DeleteCourseReq deleteCourseReq) {
        // 유저 및 코스 조회
        User user = userRepository.findUserByUserNumber(userNumber);
        Course course = courseRepository.findCourseByCourseNumber(deleteCourseReq.getCourseNumber());

        // 존재하지 않는 유저일 때
        if (user == null || course == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        // 코스 생성
        return ResponseEntity.status(HttpStatus.OK).body(courseService.deleteUserCourse(course));
    }

    // 코스 장소 삭제 API
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
    @GetMapping("/{userNumber}")
    @Operation(summary = "나의 코스 목록 조회", description = "나의 코스 목록 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "나의 코스 목록을 조회했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
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
    @Operation(summary = "관광지 저장", description = "관광지 저장 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관광지를 저장했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 관광지입니다.", content = @Content(mediaType = "application/json")),
    })
    public ResponseEntity<CourseSaveRes> saveLocation(@RequestBody SaveLocationReq saveLocationReq) {
        // 유저 및 관광지 조회
        User user = userRepository.findUserByUserNumber(saveLocationReq.getUserNumber());
        Location location = locationRepository.findLocationByContentId(saveLocationReq.getContentId());

        // 존재하지 않는 관광지일 때
        if (location == null || user == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseService.saveLocation(user, location));
    }

    // 저장한 관광지 조회 API
    @GetMapping("/save/{userNumber}")
    @Operation(summary = "저장한 관광지 조회", description = "저장한 관광지 조회 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장한 관광지를 조회했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    public ResponseEntity<List<LocationInfoRes>> saveLocationList(@PathVariable("userNumber") Long userNumber) {
        // 유저 조회
        User user = userRepository.findUserByUserNumber(userNumber);

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

    // 코스 순서 수정 API
    @PatchMapping("/{courseNumber}")
    @Operation(summary = "코스 수정", description = "코스 수정 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코스를 수정했습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 접근입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "courseNumber", description = "코스 번호", example = "32")
    public ResponseEntity<CourseLocRes> CourseUpdate(@RequestBody CourseUpdateReq courseUpdateReq) {
        // 유저 및 코스 조회
        User user = userRepository.findUserByUserNumber(courseUpdateReq.getUserNumber());
        Course course = courseRepository.findCourseByCourseNumber(courseUpdateReq.getCourseNumber());

        // 존재하지 않는 유저일 때
        if (user == null || course == null || course.getUser() != user)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        CourseLocRes courseLocRes = courseService.updateCourse(course, courseUpdateReq.getCourseLocByDay());
        if (courseLocRes == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(courseLocRes);
    }
}
