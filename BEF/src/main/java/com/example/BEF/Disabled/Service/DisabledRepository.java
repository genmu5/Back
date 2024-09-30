package com.example.BEF.Disabled.Service;


import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DisabledRepository extends JpaRepository<Disabled, Long> {
    Disabled findDisabledByLocation(Location location);

    // 노약자 필터링 (엘리베이터 또는 경로가 null이 아니고 빈 문자열이 아님)
    @Query("SELECT d FROM Disabled d WHERE d.elevator IS NOT NULL AND d.elevator <> '' OR d.route IS NOT NULL AND d.route <> ''")
    List<Disabled> findByValidElevatorOrRoute();

    // 휠체어 필터링 (엘리베이터, 출입구, 대중교통이 null이 아니고 빈 문자열이 아님)
    @Query("SELECT d FROM Disabled d WHERE d.elevator IS NOT NULL AND d.elevator <> '' AND d.entrance IS NOT NULL AND d.entrance <> '' AND d.publicTransport IS NOT NULL AND d.publicTransport <> ''")
    List<Disabled> findByValidElevatorAndEntranceAndPublicTransport();

    // 시각 장애 필터링 (점자 블록과 안내 요원이 null이 아니고 빈 문자열이 아님)
    @Query("SELECT d FROM Disabled d WHERE d.braileBlock IS NOT NULL AND d.braileBlock <> '' AND d.guideHuman IS NOT NULL AND d.guideHuman <> ''")
    List<Disabled> findByValidBraileBlockAndGuideHuman();

    // 청각 장애 필터링 (수어 가이드, 비디오 가이드, 청각 장애 전용 객실 중 하나가 null이 아니고 빈 문자열이 아님)
    @Query("SELECT d FROM Disabled d WHERE d.signGuide IS NOT NULL AND d.signGuide <> '' OR d.videoGuide IS NOT NULL AND d.videoGuide <> '' OR d.hearingRoom IS NOT NULL AND d.hearingRoom <> '' OR d.hearingHandicapEtc IS NOT NULL AND d.hearingHandicapEtc <> ''")
    List<Disabled> findByValidSignGuideOrVideoGuideOrHearingRoomOrHearingHandicapEtc();

    // 영유아 관련 필터링 (유모차, 수유실, 아기 의자가 중 하나가 null이 아니고 빈 문자열이 아님)
    @Query("SELECT d FROM Disabled d WHERE d.stroller IS NOT NULL AND d.stroller <> '' OR d.lactationRoom IS NOT NULL AND d.lactationRoom <> '' OR d.babySpareChair IS NOT NULL AND d.babySpareChair <> '' OR d.infantsFamilyEtc IS NOT NULL AND d.infantsFamilyEtc <> ''")
    List<Disabled> findByValidStrollerOrLactationRoomOrBabySpareChairOrInfantsFamilyEtc();
}
