package com.example.BEF.Search;

import com.example.BEF.Location.Domain.Location;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchRepository extends JpaRepository<Location , Long> {
    @Query("SELECT l FROM Location l WHERE l.contentTitle LIKE CONCAT('%', :keyword, '%')")
    List<Location> findByKeyword(@Param("keyword") String keyword);

}
