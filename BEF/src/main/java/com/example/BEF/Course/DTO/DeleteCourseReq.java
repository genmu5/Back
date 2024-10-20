package com.example.BEF.Course.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteCourseReq {
    @Schema(description = "코스 번호")
    private Long courseNumber;
}
