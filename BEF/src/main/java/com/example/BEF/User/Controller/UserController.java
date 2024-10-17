package com.example.BEF.User.Controller;

import com.example.BEF.User.DTO.UserJoinReq;
import com.example.BEF.User.DTO.UserJoinRes;
import com.example.BEF.User.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "유저가 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 회원가입 요청입니다.", content = @Content(mediaType = "application/json")),
    })
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
