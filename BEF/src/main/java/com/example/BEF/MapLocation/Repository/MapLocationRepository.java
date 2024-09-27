package com.example.BEF.MapLocation.Repository;

import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapLocationRepository extends CrudRepository<Location, Long> {
    //하버사인 공식을 통해 구면적기리 반경 10km 안에 있는 장소 제공
    @Query(value = "SELECT * FROM location l " +
            "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(l.gps_y)) * cos(radians(l.gps_x) - radians(:lng)) + sin(radians(:lat)) * sin(radians(l.gps_y)))) < 10",
            nativeQuery = true)
    List<Location> findLocationsWithinRadius(@Param("lat") double lat, @Param("lng") double lng);
}
