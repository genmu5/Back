package com.example.BEF.VoiceSearch.Service;

import com.example.BEF.VoiceSearch.Config.OpenAIClientConfig;
import com.example.BEF.VoiceSearch.DTO.TranscriptionRequest;
import com.example.BEF.VoiceSearch.DTO.WhisperTranscriptionRequest;
import com.example.BEF.VoiceSearch.DTO.WhisperTranscriptionResponse;
import com.example.BEF.VoiceSearch.Client.OpenAIClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpenAIClientService {

    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())  // file 필드를 호출
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }

}