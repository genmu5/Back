package com.example.BEF.Search.Repository;

import com.example.BEF.Location.Domain.Location;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Location , Long> {
    @Query("SELECT l FROM Location l WHERE l.contentTitle LIKE CONCAT('%', :keyword, '%')")
    List<Location> findByKeyword(@Param("keyword") String keyword);


    //하버사인 공식을 통해 구면적기리 반경 10km 안에 있는 장소 제공
    @Query(value = "SELECT * FROM location l " +
            "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(l.gps_y)) * cos(radians(l.gps_x) - radians(:lng)) + sin(radians(:lat)) * sin(radians(l.gps_y)))) < 10",
            nativeQuery = true)
    List<Location> findLocationsWithinRadius(@org.springframework.data.repository.query.Param("lat") double lat, @org.springframework.data.repository.query.Param("lng") double lng);
}
