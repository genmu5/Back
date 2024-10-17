package com.example.BEF.Course.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class CourseAddReq {
    @Schema(description = "관광지 번호 리스트", example = "[126510, 126523]")
    List<Long> contentIdList;
}
