package com.example.BEF.Location.Repository;

import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long>, LocationRepositoryCustom {
    Location findLocationByContentId(Long contentId);

    List<Location> findByAddrContainingAndAddrContaining(String state, String city);

    // 개요 컬럼에서 키워드가 존재하면 해당 개요 컬럼이 있는 튜플 전부 반환
    @Query("SELECT l FROM Location l WHERE l.description LIKE %:keyword%")
    List<Location>findDescription(@Param("keyword") String keyword);

    Boolean existsByContentId(Long contentId);
}