package com.example.BEF.User.DTO;

import lombok.Data;

@Data
public class UserJoinRes {
//    private Long userNumber;
    private String userName;
    private String uuid;

//    public UserJoinRes(Long userNumber, String userName) {
    public UserJoinRes(String userName, String uuid) {
//        this.userNumber = userNumber;
        this.userName = userName;
        this.uuid = uuid;
    }
}
