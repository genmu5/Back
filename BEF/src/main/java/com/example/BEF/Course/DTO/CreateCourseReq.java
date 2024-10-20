package com.example.BEF.Course.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateCourseReq {
    @Schema(description = "코스명")
    private String name;
    @Schema(description = "코스 설명")
    private String description;
}
