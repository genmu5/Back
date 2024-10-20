package com.example.BEF.Location.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class MapLocationRequest {
    private double gpsX; //경도
    private double gpsY; //위도

    public MapLocationRequest(double gpsX, double gpsY) {
        this.gpsX = gpsX;
        this.gpsY = gpsY;
    }
}
