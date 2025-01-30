package com.example.BEF.Course.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateCourseReq {
    @Schema(description = "코스 ID", example = "25")
    private Long courseNumber;

    @Schema(description = "사용자 고유 번호", example = "1")
    private Long userNumber;

    @Schema(description = "코스 이름", example = "서울 여행 코스")
    private String courseName;

    @Schema(description = "여행 시작 날짜", example = "2024-12-01")
    private LocalDate startDate;
}
