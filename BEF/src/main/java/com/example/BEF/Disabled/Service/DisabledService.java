package com.example.BEF.Disabled.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.DTO.UserDisabledDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class DisabledService {

    @Autowired
    DisabledRepository disabledRepository;

    @PersistenceContext  // EntityManager 주입
    private EntityManager entityManager;

    // 관광지 장애 정보 필터링
//    public Set<Disabled> filteringDisabled(UserDisabledDTO userDisabledDTO) {
//        Set<Disabled> filteredList = new HashSet<>();
//
//        // 노약자 필터링
//        // 필터링 : 엘리베이터 || 대중교통
//        if (userDisabledDTO.getSenior())
//            filteredList.addAll(disabledRepository.findByValidElevatorOrRoute());
//
//        // 휠체어 필터링
//        // 필터링 : 엘리베이터 && 출입통로 && 경사로
//        if (userDisabledDTO.getWheelchair())
//            filteredList.addAll(disabledRepository.findByValidElevatorAndEntranceAndPublicTransport());
//
//        // 시각 장애 필터링
//        // 필터링 : 점자블록 && 안내요원
//        if (userDisabledDTO.getBlind_handicap())
//            filteredList.addAll(disabledRepository.findByValidBraileBlockAndGuideHuman());
//
//        // 청각 장애 필터링
//        // 필터링 : 수화 || 비디오 가이드 || 객실 || 청각 장애 기타 상세
//        if (userDisabledDTO.getHearing_handicap())
//            filteredList.addAll(disabledRepository.findByValidSignGuideOrVideoGuideOrHearingRoomOrHearingHandicapEtc());
//
//        // 영유아 필터링
//        // 필터링 : 유모차 || 수유실 || 보조 의자 || 영유아 기타 상세
//        if (userDisabledDTO.getInfants_family())
//            filteredList.addAll(disabledRepository.findByValidStrollerOrLactationRoomOrBabySpareChairOrInfantsFamilyEtc());
//
//        return (filteredList);
//    }

    @Transactional
    public Set<Disabled> filterByUserDisabilityAndTravelType(UserDisabledDTO userDisabledDTO, List<String> travelTypes) {
        // 기본 SQL 쿼리
        StringBuilder queryBuilder = new StringBuilder("SELECT d.* FROM disabled d JOIN location l ON d.content_id = l.content_id " +
                "WHERE (:senior = false OR (d.elevator IS NOT NULL AND d.elevator <> '' AND d.entrance IS NOT NULL AND d.entrance <> '' AND d.public_transport IS NOT NULL AND d.public_transport <> '')) " +
                "AND (:wheelchair = false OR (d.elevator IS NOT NULL AND d.elevator <> '' AND d.entrance IS NOT NULL AND d.entrance <> '' AND d.public_transport IS NOT NULL AND d.public_transport <> '')) " +
                "AND (:blindHandicap = false OR (d.braile_block IS NOT NULL AND d.braile_block <> '' AND d.guide_human IS NOT NULL AND d.guide_human <> '')) " +
                "AND (:hearingHandicap = false OR (d.sign_guide IS NOT NULL AND d.sign_guide <> '' OR d.video_guide IS NOT NULL AND d.video_guide <> '' OR d.hearing_room IS NOT NULL AND d.hearing_room <> '' OR d.hearing_handicap_etc IS NOT NULL AND d.hearing_handicap_etc <> '')) " +
                "AND (:infantsFamily = false OR (d.stroller IS NOT NULL AND d.stroller <> '' OR d.lactation_room IS NOT NULL AND d.lactation_room <> '' OR d.baby_spare_chair IS NOT NULL AND d.baby_spare_chair <> '' OR d.infants_family_etc IS NOT NULL AND d.infants_family_etc <> '')) ");

        // travelTypes가 존재하면 각 travelType에 대해 LIKE 조건 추가
        if (travelTypes != null && !travelTypes.isEmpty()) {
            queryBuilder.append("AND (");
            for (int i = 0; i < travelTypes.size(); i++) {
                queryBuilder.append("l.description LIKE :travelType" + i);
                if (i < travelTypes.size() - 1) {
                    queryBuilder.append(" OR ");
                }
            }
            queryBuilder.append(")");
        }

        // 로그로 SQL 쿼리 출력
        log.info("Generated Query: {}", queryBuilder.toString());

        // Query 생성
        Query query = entityManager.createNativeQuery(queryBuilder.toString(), Disabled.class);

        // 파라미터 바인딩
        query.setParameter("senior", userDisabledDTO.getSenior());
        query.setParameter("wheelchair", userDisabledDTO.getWheelchair());
        query.setParameter("blindHandicap", userDisabledDTO.getBlind_handicap());
        query.setParameter("hearingHandicap", userDisabledDTO.getHearing_handicap());
        query.setParameter("infantsFamily", userDisabledDTO.getInfants_family());

        // travelTypes가 존재하면 각 travelType에 대한 파라미터 설정
        if (travelTypes != null && !travelTypes.isEmpty()) {
            for (int i = 0; i < travelTypes.size(); i++) {
                query.setParameter("travelType" + i, "%" + travelTypes.get(i) + "%");
            }
        }

        // 결과 실행 및 반환
        List<Disabled> resultList = query.getResultList();
        return new HashSet<>(resultList);  // 중복 제거를 위해 Set으로 변환
    }


    public Disabled findDisabledByLocation(Location location) {
        return disabledRepository.findDisabledByLocation(location);
    }
}
