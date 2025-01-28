package com.example.BEF.User.Service;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Disability.DisabilityRepository;
import com.example.BEF.Disability.UserDisability;
import com.example.BEF.Disability.UserDisabilityRepository;
import com.example.BEF.TripType.TripTypeRepository;
import com.example.BEF.TripType.UserTripType;
import com.example.BEF.TripType.UserTripTypeRepository;
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
    private final TripTypeRepository tripTypeRepository;
    private final UserTripTypeRepository userTripTypeRepository;
    private final DisabilityRepository disabilityRepository;
    private final UserDisabilityRepository userDisabilityRepository;

    public UserJoinRes saveUser(UserJoinReq userJoinReq) {
        // 회원가입 가능 여부 확인
        if (!userJoinReq.validJoin())
            return (null);

        // 저장할 유저 생성
        User savedUser = new User(userJoinReq.getUserName(),
                userJoinReq.getGender(),
                userJoinReq.getBirth());

        // 유저 저장
        userRepository.save(savedUser);

        // 유저 여행 타입 저장
        userTripTypeRepository.saveAll(userJoinReq.getTripType().stream()
                .map(tripTypeRepository::findByName)
                .map(tripTypeEntity -> UserTripType.of(savedUser, tripTypeEntity))
                .toList());

        // 유저 장애 유형 저장
        userDisabilityRepository.saveAll(userJoinReq.getDisability().stream()
                .map(disabilityRepository::findByName)
                .map(disabilityEntity -> UserDisability.of(savedUser, disabilityEntity))
                .toList());

        // 유저 저장한 관광지 리스트
        Course saveCourse = new Course(savedUser, "저장", "");
        courseRepository.save(saveCourse);

        // 유저 정보 리턴
        return (new UserJoinRes(savedUser.getUserNumber(), savedUser.getUserName()));
    }

//    public UserDisabledDTO settingUserDisabled(Long userNumber) {
    public UserDisabledDTO settingUserDisabled(String uuid) {
//        User disabledUser = userRepository.findUserByUserNumber(userNumber);
        User disabledUser = userRepository.findUserByUuid(uuid);

        return (new UserDisabledDTO(disabledUser.getSenior(), disabledUser.getWheelchair(),
                disabledUser.getBlindHandicap(), disabledUser.getHearingHandicap(), disabledUser.getInfantsFamily()));
    }

    public Boolean existUser(String uuid) {
        return userRepository.existsByUuid(uuid);
    }
}
