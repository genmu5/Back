package com.example.BEF.VoiceSearch.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TranscriptionRequest {
    private MultipartFile file;  // 업로드할 파일을 저장하는 필드
}