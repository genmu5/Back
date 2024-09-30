package com.example.BEF.User.Service;

import com.example.BEF.User.DTO.UserDisabledDTO;
import com.example.BEF.User.DTO.UserJoinReq;
import com.example.BEF.User.DTO.UserJoinRes;
import com.example.BEF.User.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserJoinRes saveUser(UserJoinReq userJoinReq) {
        // 저장할 유저 생성
        User savedUser = new User();

        // 회원가입 가능 여부 확인
        if (!userJoinReq.validJoin())
            return (null);

        // 유저 정보 기입
        savedUser.setUserName(userJoinReq.getUserName());
        savedUser.setGender(userJoinReq.getGender());
        savedUser.setAge(userJoinReq.getAge());
        savedUser.setSenior(userJoinReq.getSenior());
        savedUser.setWheelchair(userJoinReq.getWheelchair());
        savedUser.setBlindHandicap(userJoinReq.getBlindHandicap());
        savedUser.setHearingHandicap(userJoinReq.getHearingHandicap());
        savedUser.setInfantsFamily(userJoinReq.getInfantsFamily());

        // 유저 저장
        userRepository.save(savedUser);

        // 유저 정보 리턴
        return (new UserJoinRes(savedUser.getUserNumber(), savedUser.getUserName()));
    }

    public UserDisabledDTO settingUserDisabled(Long userNumber) {
        User disabledUser = userRepository.findUserByUserNumber(userNumber);

        return (new UserDisabledDTO(disabledUser.getSenior(), disabledUser.getWheelchair(),
                disabledUser.getBlindHandicap(), disabledUser.getHearingHandicap(), disabledUser.getInfantsFamily()));
    }
}
