package com.example.BEF.Search;

import com.example.BEF.Location.Domain.Location;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Location , Long> {
    @Query("Select l From Location l where l.contentTitle Like %:keyword%")
    List<Location> findByKeyword(@Param("keyword") String keyword);
}
