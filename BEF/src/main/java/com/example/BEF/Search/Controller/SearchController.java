package com.example.BEF.Search.Controller;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledService;
import com.example.BEF.Location.DTO.MapLocationResponse;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Search.DTO.TranscriptionRequest;
import com.example.BEF.Search.DTO.WhisperTranscriptionResponse;
import com.example.BEF.Search.Service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    private final DisabledService disabledService;

    @GetMapping("/keyword")
    public List<MapLocationResponse> searchLocations(@RequestParam("keyword") String keyword) {
        // 키워드를 기반으로 Location과 Disabled 정보를 조합한 응답 리스트를 반환
        return searchService.findByKeyword(keyword);
    }

    @GetMapping("/map")
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
    public List<MapLocationResponse> createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest) {
        WhisperTranscriptionResponse transcriptionResponse = searchService.createTranscription(transcriptionRequest);

        // 변환된 텍스트로 Location 엔티티에서 description 검색 및 MapLocationResponse 리스트로 반환
        return searchService.searchLocationsByKeyword(transcriptionResponse.getText());
    }
}
