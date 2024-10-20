package com.example.BEF.Search.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Repository.DisabledRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.DTO.MapLocationResponse;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.Search.Client.OpenAIClient;
import com.example.BEF.Search.Config.OpenAIClientConfig;
import com.example.BEF.Search.DTO.TranscriptionRequest;
import com.example.BEF.Search.DTO.WhisperTranscriptionRequest;
import com.example.BEF.Search.DTO.WhisperTranscriptionResponse;
import com.example.BEF.Search.Repository.SearchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {
    private final SearchRepository searchRepository;
    private final DisabledRepository disabledRepository;
    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;
    private final LocationRepository locationRepository;

    public List<MapLocationResponse> findByKeyword(String keyword) {

        System.out.println("Received keyword: " + keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        List<Location> locations = searchRepository.findByKeyword(keyword);

        return locations.stream()
                .map(location -> {
                    Disabled disabled = disabledRepository.findDisabledByLocation(location);

                    return new MapLocationResponse(location, disabled);
                })
                .collect(Collectors.toList());
    }

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())  // file 필드를 호출
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }

    // Keyword의 해당하는 단어 탐색하여 음성 텍스트 필터링 Keywords 배열에서 앞에 있을 수록 단어 우선순위가 높다.
    public String filterTranscription(String transcribedText) {
        List<String> Keywords = Arrays.asList("공원","바다","서울");

        return Keywords.stream()
                .filter(transcribedText::contains)
                .findFirst()
                .orElse("키워드 없음");
    }
    // location 테이블에서 키워드에 해당하는 튜플 조회후 리스트 형식으로 반환
    public List<MapLocationResponse> searchLocationsByKeyword(String transcribedText) {
        String keyword = filterTranscription(transcribedText);

        if (keyword != null) {
            List<Location> locations = locationRepository.findDescription(keyword);

            // Location 리스트를 MapLocationResponse 리스트로 변환
            return locations.stream()
                    .map(location -> {
                        Disabled disabled = disabledRepository.findDisabledByLocation(location);
                        return new MapLocationResponse(location, disabled);
                    })
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();  // 키워드가 없을 경우 빈 리스트 반환
        }
    }

    public List<Location> findLocationWithRadius(double lat, double lng){
        return searchRepository.findLocationsWithinRadius(lat, lng);
    }
}