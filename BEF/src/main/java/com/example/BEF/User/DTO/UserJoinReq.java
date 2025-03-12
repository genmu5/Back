package com.example.BEF.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserJoinReq {
    @NotBlank
    @Schema(description = "유저명")
    private String userName;
    @NotNull
    @Schema(description = "성별", example = "man / woman")
    private String gender;
    @NotNull
    @Schema(description = "생년월일")
    private LocalDate birth;
    @Schema(description = "장애 타입, MOBILITY(0), BLIND(1), HEAR(2), FAMILY(3) 중 복수 개를 문자열 리스트 형태로 요청", example = "1, 2, 3")
    private List<Long> disability;
    @Schema(description = "선호하는 여행 타입, FOREST(0), OCEAN(1), HISTORY(2), OUTSIDE(3) 중 복수 개를 문자열 리스트 형태로 요청", example = "1, 2")
    private List<Long> tripType;
}
