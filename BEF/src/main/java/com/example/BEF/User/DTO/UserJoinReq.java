package com.example.BEF.User.DTO;

import lombok.Data;

@Data
public class UserJoinReq {
    private String userName;
    private String gender;
    private Long age;
    private Boolean senior;
    private Boolean wheelchair;
    private Boolean blindHandicap;
    private Boolean hearingHandicap;
    private Boolean infantsFamily;

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
        else if (infantsFamily == null)
            return false;
        return true;
    }
}
