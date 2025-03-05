package com.example.BEF.Disabled.Service;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Disabled.Repository.DisabledRepository;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.DTO.UserDisabledDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.BEF.Disabled.Domain.QDisabled.disabled;
import static com.example.BEF.Location.Domain.QLocation.location;

@Service
@Slf4j
@RequiredArgsConstructor
public class DisabledService {

    private final DisabledRepository disabledRepository;
    private final JPAQueryFactory queryFactory;

    @PersistenceContext  // EntityManager 주입
    private EntityManager entityManager;

    @Transactional
    public Set<Disabled> filterByUserDisabilityAndTravelType(UserDisabledDTO userDisabledDTO, List<String> travelTypes) {
        BooleanBuilder whereClause = new BooleanBuilder();

        // 장애 유형 필터링
        if (userDisabledDTO.getMobility()) {
            whereClause.and(disabled.elevator.isNotNull()
                    .and(disabled.elevator.ne(""))
                    .and(disabled.entrance.isNotNull())
                    .and(disabled.entrance.ne(""))
                    .and(disabled.publicTransport.isNotNull())
                    .and(disabled.publicTransport.ne("")));
        }

        if (userDisabledDTO.getBlind()) {
            whereClause.and(disabled.braileBlock.isNotNull()
                    .and(disabled.braileBlock.ne(""))
                    .and(disabled.guideHuman.isNotNull())
                    .and(disabled.guideHuman.ne("")));
        }

        if (userDisabledDTO.getHear()) {
            whereClause.and(disabled.signGuide.isNotNull().and(disabled.signGuide.ne(""))
                    .or(disabled.videoGuide.isNotNull().and(disabled.videoGuide.ne("")))
                    .or(disabled.hearingRoom.isNotNull().and(disabled.hearingRoom.ne("")))
                    .or(disabled.hearingHandicapEtc.isNotNull().and(disabled.hearingHandicapEtc.ne(""))));
        }

        if (userDisabledDTO.getFamily()) {
            whereClause.and(disabled.stroller.isNotNull().and(disabled.stroller.ne(""))
                    .or(disabled.lactationRoom.isNotNull().and(disabled.lactationRoom.ne("")))
                    .or(disabled.babySpareChair.isNotNull().and(disabled.babySpareChair.ne("")))
                    .or(disabled.infantsFamilyEtc.isNotNull().and(disabled.infantsFamilyEtc.ne(""))));
        }

        // 여행 유형 필터링 (LIKE 검색)
        if (travelTypes != null && !travelTypes.isEmpty()) {
            BooleanBuilder travelTypeFilter = new BooleanBuilder();
            for (String type : travelTypes) {
                travelTypeFilter.or(location.description.likeIgnoreCase("%" + type + "%"));
            }
            whereClause.and(travelTypeFilter);
        }

        // Query 실행
        List<Disabled> result = queryFactory
                .selectFrom(disabled)
                .join(location).on(disabled.location.eq(location))
                .where(whereClause)
                .fetch();


        return new HashSet<>(result); // 중복 제거를 위해 Set으로 변환
    }

    public Disabled findDisabledByLocation(Location location) {
        return disabledRepository.findDisabledByLocation(location);
    }
}
