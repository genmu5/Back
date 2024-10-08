package com.example.BEF.Location.Service;

import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findLocationByContentId(Long contentId);

    List<Location> findByAddrContainingAndAddrContaining(String state, String city);
}