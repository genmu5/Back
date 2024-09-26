package com.example.BEF.Location.Service;

import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}