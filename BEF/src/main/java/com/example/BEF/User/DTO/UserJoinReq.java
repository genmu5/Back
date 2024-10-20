package com.example.BEF.User.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserJoinReq {
    @Schema(description = "유저명")
    private String userName;
    @Schema(description = "성별", example = "man / woman")
    private String gender;
    @Schema(description = "나이")
    private Long age;
    @Schema(description = "노약자 여부")
    private Boolean senior;
    @Schema(description = "휠체어 여부")
    private Boolean wheelchair;
    @Schema(description = "시각 장애 여부")
    private Boolean blindHandicap;
    @Schema(description = "청각 장애 여부")
    private Boolean hearingHandicap;
    @Schema(description = "영유아 가족 여부")
    private Boolean infantsFamily;
    @Schema(description = "선호하는 여행 타입, forest, ocean, history, outside 중 복수 개를 문자열 리스트 형태로 요청", example = "forest, ocean, history, outside")
    private List<String> travelType;

    public boolean validJoin() {
        if (userName == null || userName.isBlank())
            return false;
        else if (gender == null || gender.isBlank())
            return false;
        else if (age == null || age.equals(0L))
            return false;
        else if (senior == null)
            return false;
        else if (wheelchair == null)
            return false;
        else if (blindHandicap == null)
            return false;
        else if (hearingHandicap == null)
            return false;
        else return infantsFamily != null;
    }
}
