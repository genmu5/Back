package com.example.BEF.MapLocation.DTO;

public class MapLocationResponse {

    private String contentTitle;
    private String addr;
    private Double gpsX;
    private Double gpsY;
    private String originalImage;
    private String thumbnailImage;

    public MapLocationResponse(String contentTitle, String addr, Double gpsX, Double gpsY, String thumbnailImage, String originalImage) {
        this.contentTitle = contentTitle;
        this.addr = addr;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.originalImage = originalImage;
        this.thumbnailImage = thumbnailImage;
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
}
