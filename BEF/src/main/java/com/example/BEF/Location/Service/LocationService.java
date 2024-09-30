package com.example.BEF.Location.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledRepository;
import com.example.BEF.Disabled.Service.DisabledService;
import com.example.BEF.Location.DTO.UserLocationRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.DTO.UserDisabledDTO;
import com.example.BEF.User.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class LocationService {
    @Autowired
    UserService userService;

    @Autowired
    DisabledService disabledService;

    @Autowired
    DisabledRepository disabledRepository;

    // 관광지 필터링 - 유저 장애 정보 관련
    public List<UserLocationRes> findLocationWithDisabled(Long userNumber) {
        // 유저 장애 정보
        UserDisabledDTO userDiabledDTO = userService.settingUserDisabled(userNumber);

        // 필터링 된 관광지 장애 정보 리스트
        Set<Disabled> disabledList = disabledService.filteringDisabled(userDiabledDTO);
        List<UserLocationRes> userLocationResList = new ArrayList<>();

        // 필터링 된 관광지 정보 리스트 리턴
        for (Disabled disabled : disabledList) {
            Location location = disabled.getLocation();
            UserLocationRes userLocationRes = new UserLocationRes(location.getContentTitle(), location.getAddr(), location.getThumbnailImage());
            userLocationResList.add(userLocationRes);
        }

        log.info("관광지 리스트 개수 : {}", userLocationResList.size());
        return (userLocationResList);
    }
}
