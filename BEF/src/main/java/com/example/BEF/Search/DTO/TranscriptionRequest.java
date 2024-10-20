package com.example.BEF.Search.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class TranscriptionRequest {
    @Schema(description = "업로드할 음성 파일")
    private MultipartFile file;  // 업로드할 파일을 저장하는 필드
}