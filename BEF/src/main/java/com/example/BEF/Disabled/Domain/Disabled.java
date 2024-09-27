package com.example.BEF.Disabled.Domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Disabled")
@Getter @Setter
@NoArgsConstructor
public class Disabled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // disabled_number는 자동 생성
    @Column(name = "disabled_number")
    private Long disabledNumber;  // 장애시설코드

    @JsonProperty("contentid")
    @Column(name = "content_id", nullable = false)
    private Long contentId;       // 콘텐츠 ID (외래 키)

    @JsonProperty("parking")
    @Column(name = "parking")
    private String parking;       // 주차여부

    @JsonProperty("route")
    @Column(name = "route")
    private String route;         // 대중교통

    @JsonProperty("publictransport")
    @Column(name = "public_transport")
    private String publicTransport; // 접근로

    @JsonProperty("ticketoffice")
    @Column(name = "ticket_office")
    private String ticketOffice;  // 매표소

    @JsonProperty("promotion")
    @Column(name = "promotion")
    private String promotion;     // 홍보물

    @JsonProperty("wheelchair")
    @Column(name = "wheelchair")
    private String wheelchair;   // 휠체어

    @JsonProperty("exit")
    @Column(name = "entrance")
    private String entrance;          // 출입통로

    @JsonProperty("elevator")
    @Column(name = "elevator")
    private String elevator;      // 엘리베이터

    @JsonProperty("restroom")
    @Column(name = "restroom")
    private String restroom;      // 화장실

    @JsonProperty("auditorium")
    @Column(name = "auditorium")
    private String auditorium;    // 관람석

    @JsonProperty("room")
    @Column(name = "room")
    private String room;          // 객실

    @JsonProperty("handicapetc")
    @Column(name = "handicap_etc")
    private String handicapEtc;   // 지체장애 기타상세

    @JsonProperty("braileblock")
    @Column(name = "braile_block")
    private String braileBlock;   // 점자블록

    @JsonProperty("helpdog")
    @Column(name = "help_dog")
    private String helpDog;       // 보조견동반

    @JsonProperty("guidehuman")
    @Column(name = "guide_human")
    private String guideHuman;    // 안내요원

    @JsonProperty("audioguide")
    @Column(name = "audio_guide")
    private String audioGuide;    // 오디오가이드

    @JsonProperty("bigprint")
    @Column(name = "big_print")
    private String bigPrint;      // 큰활자 홍보물

    @JsonProperty("brailepromotion")
    @Column(name = "braile_promotion")
    private String brailePromotion; // 점자홍보물 및 점자표지판

    @JsonProperty("guidesystem")
    @Column(name = "guide_system")
    private String guideSystem;   // 유도안내설비

    @JsonProperty("blindhandicapetc")
    @Column(name = "blind_handicap_etc")
    private String blindHandicapEtc; // 시각장애 기타상세

    @JsonProperty("signguide")
    @Column(name = "sign_guide")
    private String signGuide;     // 수화안내

    @JsonProperty("videoguide")
    @Column(name = "video_guide")
    private String videoGuide;    // 자막 비디오가이드 및 영상자막안내

    @JsonProperty("hearingroom")
    @Column(name = "hearing_room")
    private String hearingRoom;   // 청각장애 객실

    @JsonProperty("hearinghandicapetc")
    @Column(name = "hearing_handicap_etc")
    private String hearingHandicapEtc; // 청각 장애 기타 상세

    @JsonProperty("stroller")
    @Column(name = "stroller")
    private String stroller;      // 유모차

    @JsonProperty("lactationroom")
    @Column(name = "lactation_room")
    private String lactationRoom; // 수유실

    @JsonProperty("babysparechair")
    @Column(name = "baby_spare_chair")
    private String babySpareChair; // 유아용보조의자

    @JsonProperty("infantsfamilyetc")
    @Column(name = "infants_family_etc")
    private String infantsFamilyEtc; // 영유아가족 기타상세

    public Disabled(Long contentId, String parking, String route, String publicTransport, String ticketOffice,
                    String promotion, String wheelchair, String entrance, String elevator, String restroom,
                    String auditorium, String room, String handicapEtc, String braileBlock, String helpDog,
                    String guideHuman, String audioGuide, String bigPrint, String brailePromotion, String guideSystem,
                    String blindHandicapEtc, String signGuide, String videoGuide, String hearingRoom,
                    String hearingHandicapEtc, String stroller, String lactationRoom, String babySpareChair,
                    String infantsFamilyEtc) {
        this.contentId = contentId;
        this.parking = parking;
        this.route = route;
        this.publicTransport = publicTransport;
        this.ticketOffice = ticketOffice;
        this.promotion = promotion;
        this.wheelchair = wheelchair;
        this.entrance = entrance;
        this.elevator = elevator;
        this.restroom = restroom;
        this.auditorium = auditorium;
        this.room = room;
        this.handicapEtc = handicapEtc;
        this.braileBlock = braileBlock;
        this.helpDog = helpDog;
        this.guideHuman = guideHuman;
        this.audioGuide = audioGuide;
        this.bigPrint = bigPrint;
        this.brailePromotion = brailePromotion;
        this.guideSystem = guideSystem;
        this.blindHandicapEtc = blindHandicapEtc;
        this.signGuide = signGuide;
        this.videoGuide = videoGuide;
        this.hearingRoom = hearingRoom;
        this.hearingHandicapEtc = hearingHandicapEtc;
        this.stroller = stroller;
        this.lactationRoom = lactationRoom;
        this.babySpareChair = babySpareChair;
        this.infantsFamilyEtc = infantsFamilyEtc;
    }
}
