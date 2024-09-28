package com.example.BEF.User.DTO;

import lombok.Data;

@Data
public class UserJoinRes {
    private Long userNumber;
    private String userName;

    public UserJoinRes(Long userNumber, String userName) {
        this.userNumber = userNumber;
        this.userName = userName;
    }
}
