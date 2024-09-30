package com.example.BEF.Disabled.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.DTO.UserDisabledDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DisabledService {

    @Autowired
    DisabledRepository disabledRepository;

    // 관광지 장애 정보 필터링
    public Set<Disabled> filteringDisabled(UserDisabledDTO userDisabledDTO) {
        Set<Disabled> filteredList = new HashSet<>();

        // 노약자 필터링
        // 필터링 : 엘리베이터 || 대중교통
        if (userDisabledDTO.getSenior())
            filteredList.addAll(disabledRepository.findByValidElevatorOrRoute());

        // 휠체어 필터링
        // 필터링 : 엘리베이터 && 출입통로 && 경사로
        if (userDisabledDTO.getWheelchair())
            filteredList.addAll(disabledRepository.findByValidElevatorAndEntranceAndPublicTransport());

        // 시각 장애 필터링
        // 필터링 : 점자블록 && 안내요원
        if (userDisabledDTO.getBlind_handicap())
            filteredList.addAll(disabledRepository.findByValidBraileBlockAndGuideHuman());

        // 청각 장애 필터링
        // 필터링 : 수화 || 비디오 가이드 || 객실 || 청각 장애 기타 상세
        if (userDisabledDTO.getHearing_handicap())
            filteredList.addAll(disabledRepository.findByValidSignGuideOrVideoGuideOrHearingRoomOrHearingHandicapEtc());

        // 영유아 필터링
        // 필터링 : 유모차 || 수유실 || 보조 의자 || 영유아 기타 상세
        if (userDisabledDTO.getInfants_family())
            filteredList.addAll(disabledRepository.findByValidStrollerOrLactationRoomOrBabySpareChairOrInfantsFamilyEtc());

        return (filteredList);
    }


    public Disabled findDisabledByLocation(Location location) {
        return disabledRepository.findDisabledByLocation(location);
    }

}
