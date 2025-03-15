package com.example.BEF.User.Service;

import com.example.BEF.Course.DTO.UserCourseRes;
import com.example.BEF.Course.Repository.CourseRepository;
import com.example.BEF.Course.Repository.UserCourseRepository;
import com.example.BEF.Disability.*;
import com.example.BEF.TripType.TripTypeRepository;
import com.example.BEF.TripType.UserTripType;
import com.example.BEF.TripType.UserTripTypeRepository;
import com.example.BEF.User.DTO.UserDisabledDTO;
import com.example.BEF.User.DTO.UserJoinReq;
import com.example.BEF.User.DTO.UserJoinRes;
import com.example.BEF.User.Domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final TripTypeRepository tripTypeRepository;
    private final UserTripTypeRepository userTripTypeRepository;
    private final DisabilityRepository disabilityRepository;
    private final UserDisabilityRepository userDisabilityRepository;

    private boolean validJoin(UserJoinReq userJoinReq) {
        if (isBlank(userJoinReq.getUserName())
                || isBlank(userJoinReq.getGender())
                || userJoinReq.getBirth() == null)
            return false;

        // 장애 유형 검증
        if (!isValidList(userJoinReq.getDisability(), disabilityRepository::existsByDisabilityNumber))
            return false;

        // 선호 여행 타입 검증
        return isValidList(userJoinReq.getTripType(), tripTypeRepository::existsByTripTypeNumber);
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
    }

    private <T> boolean isValidList(List<T> list, Predicate<T> existsCheck) {
        return list != null && !list.isEmpty() && list.stream().allMatch(existsCheck);
    }

    @Transactional
    public UserJoinRes saveUser(UserJoinReq userJoinReq) {
        // 회원가입 가능 여부 확인
        if (!validJoin(userJoinReq))
            return (null);

        // 저장할 유저 생성
        User savedUser = new User(userJoinReq.getUserName(),
                userJoinReq.getGender(),
                userJoinReq.getBirth());

        // 유저 저장
        userRepository.save(savedUser);

        // 유저 여행 타입 저장
        userTripTypeRepository.saveAll(userJoinReq.getTripType().stream()
                .map(tripTypeRepository::findTripTypeEntityByTripTypeNumber)
                .map(tripTypeEntity -> UserTripType.of(savedUser, tripTypeEntity))
                .toList());

        // 유저 장애 유형 저장
        userDisabilityRepository.saveAll(userJoinReq.getDisability().stream()
                .map(disabilityRepository::findDisabilityByDisabilityNumber)
                .map(disabilityEntity -> UserDisability.of(savedUser, disabilityEntity))
                .toList());

        // 유저 정보 리턴
        return (new UserJoinRes(savedUser.getUserNumber(), savedUser.getUserName()));
    }

    public UserDisabledDTO settingUserDisabled(Long userNumber) {
        User disabledUser = userRepository.findUserByUserNumber(userNumber);


        return (UserDisabledDTO.of(userDisabilityRepository.existsByUserAndDisability(disabledUser, disabilityRepository.findByName("MOBILITY")),
                userDisabilityRepository.existsByUserAndDisability(disabledUser, disabilityRepository.findByName("BLIND")),
                userDisabilityRepository.existsByUserAndDisability(disabledUser, disabilityRepository.findByName("HEAR")),
                userDisabilityRepository.existsByUserAndDisability(disabledUser, disabilityRepository.findByName("FAMILY"))));
    }

    public Boolean existUser(Long userNumber) {
        return userRepository.existsByUserNumber(userNumber);
    }

    public List<UserCourseRes> getCourseList(Long userNumber) {
        return courseRepository.findAllByUser(userRepository.findUserByUserNumber(userNumber)).stream()
                .map(course -> {
                    var userCourse = userCourseRepository.findFirstByCourseAndDay(course, 1L);
                    String originalImage = (userCourse != null && userCourse.getLocation() != null)
                            ? userCourse.getLocation().getOriginalImage()
                            : null;

                    // originalImage가 null인 경우 건너뛰기
                    return originalImage != null ? UserCourseRes.builder()
                            .course(course)
                            .originalImage(originalImage)
                            .build() : null;
                })
                .filter(Objects::nonNull) // null을 필터링
                .toList();
    }
}
