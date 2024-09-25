package com.example.BEF.VoiceSearch;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIClientConfig {

    // application.yml에서 API 키를 읽어옴
    @Value("${openai-service.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // API 요청 헤더에 Authorization 헤더 추가
            requestTemplate.header("Authorization", "Bearer " + apiKey);
        };
    }

    @Bean
    public String getAudioModel() {
        return "whisper-1"; // Whisper 모델 이름
    }
}
