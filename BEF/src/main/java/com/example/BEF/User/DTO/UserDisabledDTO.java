package com.example.BEF.User.DTO;

import lombok.Data;

@Data
public class UserDisabledDTO {
    private Boolean senior;
    private Boolean wheelchair;
    private Boolean blind_handicap;
    private Boolean hearing_handicap;
    private Boolean infants_family;

    public UserDisabledDTO(Boolean senior, Boolean wheelchair, Boolean blind_handicap, Boolean hearing_handicap, Boolean infants_family) {
        this.senior = senior;
        this.wheelchair = wheelchair;
        this.blind_handicap = blind_handicap;
        this.hearing_handicap = hearing_handicap;
        this.infants_family = infants_family;
    }
}
