package com.example.BEF.MapLocation.Controller;

import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.MapLocation.DTO.MapLocationResponse;
import com.example.BEF.Disabled.Service.DisabledService;
import com.example.BEF.MapLocation.Service.MapLocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class MapLocationController {

    private final MapLocationService mapLocationService;
    private final DisabledService disabledService;

    public MapLocationController(MapLocationService mapLocationService, DisabledService disabledService) {
        this.mapLocationService = mapLocationService;
        this.disabledService = disabledService;
    }

    @GetMapping("/map")
    public List<MapLocationResponse> getMapLocations(@RequestParam("gpsX") double gpsX, @RequestParam("gpsY") double gpsY) {
        List<Location> locations = mapLocationService.findLocationWithRadius(gpsY, gpsX);

        // Location을 MapLocationResponse로 변환하여 반환
        return locations.stream()
                .map(location -> {
                    Disabled disabled = disabledService.findDisabledByLocation(location);
                    return new MapLocationResponse(location, disabled); // 수정된 생성자 사용
                }).collect(Collectors.toList());
    }

}
