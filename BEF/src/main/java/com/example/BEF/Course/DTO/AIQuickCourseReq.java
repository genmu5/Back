package com.example.BEF.Course.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "AI 추천 코스 요청 객체")
public class AIQuickCourseReq
{
    private Long userNumber;

    @Schema(description = "여행할 지역 ID", example = "1", required = true)
    private Long area;

    @Schema(description = "여행 기간 (일 단위)", example = "3", required = true)
    private Long period;
}