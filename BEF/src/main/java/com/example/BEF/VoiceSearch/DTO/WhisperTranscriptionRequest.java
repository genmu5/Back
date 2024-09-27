package com.example.BEF.VoiceSearch.DTO;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class WhisperTranscriptionRequest {
    private String model;
    private MultipartFile file;
}