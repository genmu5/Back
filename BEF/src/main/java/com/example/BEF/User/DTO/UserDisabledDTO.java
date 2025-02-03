package com.example.BEF.User.DTO;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDisabledDTO {
    private Boolean mobility;
    private Boolean blind;
    private Boolean hear;
    private Boolean family;

    @Builder
    private UserDisabledDTO(Boolean mobility, Boolean blind, Boolean hear, Boolean family) {
        this.mobility = mobility;
        this.blind = blind;
        this.hear = hear;
        this.family = family;
    }

    public static UserDisabledDTO of(Boolean mobility, Boolean blind, Boolean hear, Boolean family) {
        return UserDisabledDTO.builder()
                .mobility(mobility)
                .blind(blind)
                .hear(hear)
                .family(family)
                .build();
    }
}
