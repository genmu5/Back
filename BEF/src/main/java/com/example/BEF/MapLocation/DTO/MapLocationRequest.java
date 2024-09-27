package com.example.BEF.MapLocation.DTO;

public class MapLocationRequest {
    private double gpsX; //경도
    private double gpsY; //위도

    public MapLocationRequest() {};
    public MapLocationRequest(double gpsX, double gpsY) {
        this.gpsX = gpsX;
        this.gpsY = gpsY;
    }

    public double getGpsX() {
        return gpsX;
    }

    public void setGpsX(double gpsX) {
        this.gpsX = gpsX;
    }

    public double getGpsY() {
        return gpsY;
    }

    public void setGpsY(double gpsY) {
        this.gpsY = gpsY;
    }
}
