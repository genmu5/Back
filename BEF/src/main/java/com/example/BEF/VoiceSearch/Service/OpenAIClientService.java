package com.example.BEF.VoiceSearch.Service;

import com.example.BEF.Location.Domain.Location;
import com.example.BEF.VoiceSearch.Config.OpenAIClientConfig;
import com.example.BEF.VoiceSearch.DTO.TranscriptionRequest;
import com.example.BEF.VoiceSearch.DTO.WhisperTranscriptionRequest;
import com.example.BEF.VoiceSearch.DTO.WhisperTranscriptionResponse;
import com.example.BEF.VoiceSearch.Client.OpenAIClient;
import com.example.BEF.VoiceSearch.Repository.DescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenAIClientService {

    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;
    private final DescriptionRepository descriptionRepository;

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())  // file 필드를 호출
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }

    public String filterTranscription(String transcribedText) {
        List<String> Keywords = Arrays.asList("공원","바다","서울");

        return Keywords.stream()
                .filter(transcribedText::contains)
                .findFirst()
                .orElse("키워드 없음");
    }

    public List<Location> searchLocationsByKeyword(String transcribedText) {
        String keyword = filterTranscription(transcribedText);

        if (keyword != null) {
            return descriptionRepository.findDescription(keyword);
        } else {
            return new ArrayList<>();  // 키워드가 없을 경우 빈 리스트 반환
        }
    }
}