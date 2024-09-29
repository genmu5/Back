package com.example.BEF.User.Controller;

import com.example.BEF.User.DTO.UserJoinReq;
import com.example.BEF.User.DTO.UserJoinRes;
import com.example.BEF.User.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/join")
    public ResponseEntity<UserJoinRes> join(@RequestBody UserJoinReq userJoinReq) {

        UserJoinRes savedUserJoinRes = userService.saveUser(userJoinReq);

        // 회원가입 실패시
        if (savedUserJoinRes == null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        // 회원가입 성공시
        else
            return new ResponseEntity<>(savedUserJoinRes, HttpStatus.CREATED);
    }
}
