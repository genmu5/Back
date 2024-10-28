package com.example.BEF.User.Service;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.User.DTO.UserDisabledDTO;
import com.example.BEF.User.DTO.UserJoinReq;
import com.example.BEF.User.DTO.UserJoinRes;
import com.example.BEF.User.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

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
        savedUser.setUuid(userJoinReq.getUuid()); // uuid

        // 유저 여행 타입 설정
        savedUser.setForest(userJoinReq.getTravelType().contains("forest"));
        savedUser.setOcean(userJoinReq.getTravelType().contains("ocean"));
        savedUser.setCulture(userJoinReq.getTravelType().contains("culture"));
        savedUser.setOutside(userJoinReq.getTravelType().contains("outside"));

        // 유저 저장
        userRepository.save(savedUser);

        // 유저 저장한 관광지 리스트
        Course saveCourse = new Course(savedUser, "저장", "");
        courseRepository.save(saveCourse);

        // 유저 정보 리턴
        return (new UserJoinRes(savedUser.getUserNumber(), savedUser.getUserName(), savedUser.getUuid())); // uuid
    }

    public UserDisabledDTO settingUserDisabled(Long userNumber) {
        User disabledUser = userRepository.findUserByUserNumber(userNumber);

        return (new UserDisabledDTO(disabledUser.getSenior(), disabledUser.getWheelchair(),
                disabledUser.getBlindHandicap(), disabledUser.getHearingHandicap(), disabledUser.getInfantsFamily()));
    }
}
