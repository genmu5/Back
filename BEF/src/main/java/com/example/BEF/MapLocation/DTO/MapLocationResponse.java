package com.example.BEF.MapLocation.DTO;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;

public class MapLocationResponse {

    //location 관련 컬럼
    private String contentTitle;
    private String addr;
    private Double gpsX;
    private Double gpsY;
    private String originalImage;
    private String thumbnailImage;

    //Disabled 관련 컬럼

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




    public MapLocationResponse(Location location, Disabled disabled) {
        this.contentTitle = location.getContentTitle();
        this.addr = location.getAddr();
        this.gpsX = location.getGpsX();
        this.gpsY = location.getGpsY();
        this.originalImage = location.getOriginalImage();
        this.thumbnailImage = location.getThumbnailImage();

        // Disabled 객체가 null인지 확인 후 값 설정
        if (disabled != null) {
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

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Double getGpsX() {
        return gpsX;
    }

    public void setGpsX(Double gpsX) {
        this.gpsX = gpsX;
    }

    public Double getGpsY() {
        return gpsY;
    }

    public void setGpsY(Double gpsY) {
        this.gpsY = gpsY;
    }

    public String getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getPublicTransport() {
        return publicTransport;
    }

    public void setPublicTransport(String publicTransport) {
        this.publicTransport = publicTransport;
    }

    public String getElevator() {
        return elevator;
    }

    public void setElevator(String elevator) {
        this.elevator = elevator;
    }

    public String getRestroom() {
        return restroom;
    }

    public void setRestroom(String restroom) {
        this.restroom = restroom;
    }

    public String getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(String wheelchair) {
        this.wheelchair = wheelchair;
    }

    public String getHelpDog() {
        return helpDog;
    }

    public void setHelpDog(String helpDog) {
        this.helpDog = helpDog;
    }

    public String getGuideHuman() {
        return guideHuman;
    }

    public void setGuideHuman(String guideHuman) {
        this.guideHuman = guideHuman;
    }

    public String getBraileBlock() {
        return braileBlock;
    }

    public void setBraileBlock(String braileBlock) {
        this.braileBlock = braileBlock;
    }

    public String getSignGuide() {
        return signGuide;
    }

    public void setSignGuide(String signGuide) {
        this.signGuide = signGuide;
    }

    public String getVideoGuide() {
        return videoGuide;
    }

    public void setVideoGuide(String videoGuide) {
        this.videoGuide = videoGuide;
    }

    public String getHearingHandicapEtc() {
        return hearingHandicapEtc;
    }

    public void setHearingHandicapEtc(String hearingHandicapEtc) {
        this.hearingHandicapEtc = hearingHandicapEtc;
    }

    public String getStroller() {
        return stroller;
    }

    public void setStroller(String stroller) {
        this.stroller = stroller;
    }

    public String getLactationRoom() {
        return lactationRoom;
    }

    public void setLactationRoom(String lactationRoom) {
        this.lactationRoom = lactationRoom;
    }

    public String getBabySpareChair() {
        return babySpareChair;
    }

    public void setBabySpareChair(String babySpareChair) {
        this.babySpareChair = babySpareChair;
    }
}
