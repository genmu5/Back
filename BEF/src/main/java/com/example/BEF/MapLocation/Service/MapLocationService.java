package com.example.BEF.MapLocation.Service;

import com.example.BEF.Location.Domain.Location;
import com.example.BEF.MapLocation.Repository.MapLocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapLocationService {
    private final MapLocationRepository mapLocationRepository;

    public MapLocationService(MapLocationRepository mapLocationRepository) {
        this.mapLocationRepository = mapLocationRepository;
    }

    public List<Location> findLocationWithRadius(double lat, double lng){
        return mapLocationRepository.findLocationsWithinRadius(lat, lng);
    }
}
