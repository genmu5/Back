package com.example.BEF.Location.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Repository.DisabledRepository;
import com.example.BEF.Disabled.Service.DisabledService;
import com.example.BEF.Location.DTO.DetailedInformationResponse;
import com.example.BEF.Location.DTO.LocationInfoRes;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.Location.Repository.LocationRepository;
import com.example.BEF.TripType.TripTypeRepository;
import com.example.BEF.TripType.UserTripTypeRepository;
import com.example.BEF.User.DTO.UserDisabledDTO;
import com.example.BEF.User.Domain.User;
import com.example.BEF.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final UserService userService;
    private final  DisabledService disabledService;
    private final LocationRepository locationRepository;
    private final DisabledRepository disabledRepository;
    private final UserTripTypeRepository userTripTypeRepository;
    private final TripTypeRepository tripTypeRepository;

    // 관광지 필터링 - 유저 장애 정보 관련
    public List<LocationInfoRes> filteringLocations(User user) {
        // 필터링된 관광지 리스트
        List<LocationInfoRes> locationInfoResList = new ArrayList<>();

        // 여행 타입별 단어 리스트
        List<String> userType = new ArrayList<>();

        if (userTripTypeRepository.existsByUserAndTripType(user, tripTypeRepository.findByName("FOREST")))
            userType.addAll(Arrays.asList("숲", "휴양림", "산림욕장", "치유"));
        if (userTripTypeRepository.existsByUserAndTripType(user, tripTypeRepository.findByName("OCEAN")))
            userType.addAll(Arrays.asList("해수욕장", "바다", "물놀이", "호수"));
        if (userTripTypeRepository.existsByUserAndTripType(user, tripTypeRepository.findByName("HISTORY")))
            userType.addAll(Arrays.asList("박물관", "미술관", "역사", "문화", "사찰"));
        if (userTripTypeRepository.existsByUserAndTripType(user, tripTypeRepository.findByName("OUTSIDE")))
            userType.addAll(Arrays.asList("가족", "어린이", "공원", "파크", "레저"));

        // 유저 장애 정보
        UserDisabledDTO userDisabledDTO = userService.settingUserDisabled(user.getUserNumber());

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

        return (locationInfoResList);
    }

    // 관광지 필터링 - 지역 관련
    public List<LocationInfoRes> findLocationWithDistrict(String state, String city) {

        // 필터링 된 관광지 리스트 + 지역구 리스트
        List<LocationInfoRes> locationInfoResList = new ArrayList<>();

        // 지역 검색어
        if (city.equals("전체"))
            city = "";

        List<Location> locationList = locationRepository.findByAddrContainingAndAddrContaining(state, city);

        // 필터링 된 관광지 정보 리스트 리턴
        for (Location location : locationList) {
            Disabled disabled = disabledRepository.findDisabledByLocation(location);
            LocationInfoRes locationInfoRes = new LocationInfoRes(location, disabled);
            locationInfoResList.add(locationInfoRes);
        }

        return (locationInfoResList);
    }

    public DetailedInformationResponse getLocationDetailed(Long contetntId){
        Location location = locationRepository.findLocationByContentId(contetntId);
        Disabled disabled = disabledRepository.findDisabledByLocation(location);

        return new DetailedInformationResponse(location, disabled);
    }
}