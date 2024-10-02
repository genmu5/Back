package com.example.BEF.Location.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Service.DisabledRepository;
import com.example.BEF.Disabled.Service.DisabledService;
import com.example.BEF.Location.DTO.LocationInfoRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.DTO.UserDisabledDTO;
import com.example.BEF.User.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    LocationRepository locationRepository;

    // 관광지 필터링 - 유저 장애 정보 관련
    public List<LocationInfoRes> filteringLocations(List<String> travelType, Long userNumber) {
        // 필터링된 관광지 리스트
        List<LocationInfoRes> locationInfoResList = new ArrayList<>();

        // 여행 타입별 단어 리스트
        List<String> userType = new ArrayList<>();

        if (travelType.contains("forest"))
            userType.addAll(Arrays.asList("숲", "휴양림", "산림욕장", "치유"));
        if (travelType.contains("ocean"))
            userType.addAll(Arrays.asList("해수욕장", "바다", "물놀이", "호수"));
        if (travelType.contains("history"))
            userType.addAll(Arrays.asList("박물관", "미술관", "역사", "문화", "사찰"));
        if (travelType.contains("outside"))
            userType.addAll(Arrays.asList("가족", "어린이", "공원", "파크", "레저"));

        // 유저 장애 정보
        UserDisabledDTO userDisabledDTO = userService.settingUserDisabled(userNumber);

        // 필터링 된 관광지 장애 정보 리스트
        Set<Disabled> filteredDisabledList = disabledService.filterByUserDisabilityAndTravelType(userDisabledDTO, userType);

        // 필터링 된 관광지 정보 리스트 리턴 - MAX 20
        Long idx = 0L;
        for (Disabled disabled : filteredDisabledList) {
            if (idx == 20L)
                break;
            Location location = disabled.getLocation();
            LocationInfoRes userLocationRes = new LocationInfoRes(location, disabled);
            locationInfoResList.add(userLocationRes);
            idx++;
        }

        log.info("관광지 리스트 개수 : {}", locationInfoResList.size());
        return (locationInfoResList);
    }

    // 관광지 필터링 - 지역 관련
    public List<LocationInfoRes> findLocationWithDistrict(String district) {

        // 필터링 된 관광지 리스트
        List<LocationInfoRes> locationInfoResList = new ArrayList<>();
        List<Location> locationList = locationRepository.findByAddrContaining(district);

        // 필터링 된 관광지 정보 리스트 리턴
        for (Location location : locationList) {
            Disabled disabled = disabledRepository.findDisabledByLocation(location);
            LocationInfoRes locationInfoRes = new LocationInfoRes(location, disabled);
            locationInfoResList.add(locationInfoRes);
        }

        log.info("관광지 리스트 개수 : {}", locationInfoResList.size());
        return (locationInfoResList);
    }
}
