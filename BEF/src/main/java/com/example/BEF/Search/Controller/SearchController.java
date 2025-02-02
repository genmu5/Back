package com.example.BEF.Search.Controller;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledService;
import com.example.BEF.Location.DTO.MapLocationResponse;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Search.DTO.TranscriptionRequest;
import com.example.BEF.Search.DTO.WhisperTranscriptionResponse;
import com.example.BEF.Search.Service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "검색 관련 API")
public class SearchController {

    private final SearchService searchService;
    private final DisabledService disabledService;

    @GetMapping("/keyword")
    @Operation(summary = "키워드 기반 검색", description = "키워드 기반 관광지를 검색할 때 사용하는 API")
    @ApiResponse(responseCode = "200", description = "키워드 기반으로 관광지 검색에 성공했습니다.", content = @Content(mediaType = "application/json"))
    @Parameter(name = "keyword", description = "검색 키워드", example = "우주")
    public List<MapLocationResponse> searchLocations(@RequestParam("keyword") String keyword) {
        return searchService.searchLocationsByKeyword(keyword);
    }

    @GetMapping("/map")
    @Operation(summary = "주변 관광지 검색", description = "내 위치 주변 관광지를 검색할 때 사용하는 API")
    @ApiResponse(responseCode = "200", description = "내 위치 주변 관광지 검색에 성공했습니다.", content = @Content(mediaType = "application/json"))
    @Parameters({
            @Parameter(name = "gpsX", description = "x좌표", example = "128.1"),
            @Parameter(name = "gpsY", description = "y좌표", example = "36.1")
    })
    public List<MapLocationResponse> getMapLocations(@RequestParam("gpsX") double gpsX, @RequestParam("gpsY") double gpsY) {
        List<Location> locations = searchService.findLocationWithRadius(gpsY, gpsX);

        // Location을 MapLocationResponse로 변환하여 반환
        return locations.stream()
                .map(location -> {
                    Disabled disabled = disabledService.findDisabledByLocation(location);
                    return new MapLocationResponse(location, disabled); // 수정된 생성자 사용
                }).collect(Collectors.toList());
    }

    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "음성 기반 검색", description = "음성 기반으로 관광지를 검색할 때 사용하는 API")
    @ApiResponse(responseCode = "200", description = "음성 기반 관광지 검색에 성공했습니다.", content = @Content(mediaType = "application/json"))
    public List<MapLocationResponse> createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest) {
        WhisperTranscriptionResponse transcriptionResponse = searchService.createTranscription(transcriptionRequest);

        return searchService.searchLocationsByKeyword(transcriptionResponse.getText());
    }
}
