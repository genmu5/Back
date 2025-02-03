package com.example.BEF.Course.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SaveLocationReq {
    @Schema(description = "유저 번호")
    private Long userNumber;

    @Schema(description = "관광지 번호")
    private Long contentId;
}
