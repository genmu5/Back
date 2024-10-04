package com.example.BEF.VoiceSearch.Controller;

import com.example.BEF.Location.Domain.Location;
import com.example.BEF.MapLocation.DTO.MapLocationResponse;
import com.example.BEF.VoiceSearch.DTO.TranscriptionRequest;
import com.example.BEF.VoiceSearch.DTO.WhisperTranscriptionResponse;
import com.example.BEF.VoiceSearch.Service.OpenAIClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class WhisperController {

    private final OpenAIClientService openAIClientService;

    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<MapLocationResponse> createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest) {
        WhisperTranscriptionResponse transcriptionResponse = openAIClientService.createTranscription(transcriptionRequest);

        // 변환된 텍스트로 Location 엔티티에서 description 검색 및 MapLocationResponse 리스트로 반환
        return openAIClientService.searchLocationsByKeyword(transcriptionResponse.getText());
    }
}
