package com.example.BEF.Location.Controller;

import com.example.BEF.Location.DTO.DetailedInformationResponse;
import com.example.BEF.Location.DTO.LocationInfoRes;
import com.example.BEF.Location.Service.LocationService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
@Tag(name = "Location", description = "관광지 관련 API")
public class LocationController {
    private final LocationService locationService;
    private final UserRepository userRepository;

    // 유저 장애 정보 기반 관광지 리스트 검색
    @GetMapping("/recommend")
    @Operation(summary = "유저 맞춤 관광지 리스트", description = "유저 장애 정보 + 여행 타입 맞춤 관광지 추천 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "필터링된 관광지 리스트입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 유저입니다.", content = @Content(mediaType = "application/json")),
    })
//    @Parameter(name = "userNumber", description = "유저 번호", example = "32")
    @Parameter(name = "uuid", description = "uuid", example = "32")
//    public ResponseEntity<List<LocationInfoRes>> getRecLocations(@RequestParam("userNumber") Long userNumber) {
    public ResponseEntity<List<LocationInfoRes>> getRecLocations(@RequestParam("uuid") String uuid) {
        // 존재하지 않는 유저인 경우
//        User user = userRepository.findUserByUserNumber(userNumber) == null;
        User user = userRepository.findUserByUuid(uuid);
        if (user == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        // 필터링된 관광지 리스트 리턴
        return new ResponseEntity<>(locationService.filteringLocations(user), HttpStatus.OK);
    }

    // 지역(구) 기반 관광지 리스트 검색
    @GetMapping("/district")
    @Operation(summary = "지역(구) 기반 관광지 리스트 검색", description = "지역(구) 기반 관광지 리스트 검색 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "필터링된 관광지 리스트입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 지역명입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameters({
            @Parameter(name = "state", description = "시", example = "서울특별시"),
            @Parameter(name = "city", description = "구/군", example = "강서구")
    })
    public ResponseEntity<List<LocationInfoRes>> getDistrictLocations(@RequestParam("state") String state, @RequestParam("city") String city) {
        // 존재하지 않는 지역명인 경우
        if (state == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        // 필터링된 관광지 리스트 리턴
        return new ResponseEntity<>(locationService.findLocationWithDistrict(state, city), HttpStatus.OK);
    }

    @GetMapping("/detail")
    @Operation(summary = "관광지 상세 정보", description = "관광지 상세 정보 리턴 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "필터링된 관광지 리스트입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 지역명입니다.", content = @Content(mediaType = "application/json")),
    })
    @Parameter(name = "contentid", description = "관광지 번호", example = "125503")
    public DetailedInformationResponse getDetailedInformation(@RequestParam long contentid) {
        return locationService.getLocationDetailed(contentid);
    }
}
