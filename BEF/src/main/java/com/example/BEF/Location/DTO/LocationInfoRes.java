package com.example.BEF.Location.DTO;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;
import lombok.Data;

@Data
public class LocationInfoRes {
    //location 관련
    private String contentTitle;
    private String addr;
    private Double gpsX;
    private Double gpsY;
    private String originalImage;
    private String thumbnailImage;

    //Disabled 관련

    //휠체어 사용자 && 노약자
    private String publicTransport;
    private String elevator;
    private String restroom;
    private String wheelchair;

    //시각 장애인
    private String helpDog;
    private String guideHuman;
    private String braileBlock;

    //청각 장애인
    private String signGuide;
    private String videoGuide;
    private String hearingHandicapEtc;

    //영유아
    private String stroller;
    private String lactationRoom;
    private String babySpareChair;

    public LocationInfoRes(Location location, Disabled disabled) {
        this.contentTitle = location.getContentTitle();
        this.addr = location.getAddr();
        this.gpsX = location.getGpsX();
        this.gpsY = location.getGpsY();
        this.originalImage = location.getOriginalImage();
        this.thumbnailImage = location.getThumbnailImage();

        this.publicTransport = disabled.getPublicTransport();
        this.elevator = disabled.getElevator();
        this.restroom = disabled.getRestroom();
        this.wheelchair = disabled.getWheelchair();

        this.helpDog = disabled.getHelpDog();
        this.guideHuman = disabled.getGuideHuman();
        this.braileBlock = disabled.getBraileBlock();

        this.signGuide = disabled.getSignGuide();
        this.videoGuide = disabled.getVideoGuide();
        this.hearingHandicapEtc = disabled.getHearingHandicapEtc();

        this.stroller = disabled.getStroller();
        this.lactationRoom = disabled.getLactationRoom();
        this.babySpareChair = disabled.getBabySpareChair();
    }
}
