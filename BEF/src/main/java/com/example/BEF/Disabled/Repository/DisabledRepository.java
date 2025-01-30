package com.example.BEF.Disabled.Repository;


import com.example.BEF.Disabled.Domain.Disabled;
import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisabledRepository extends JpaRepository<Disabled, Long> {
    Disabled findDisabledByLocation(Location location);
    Boolean existsByLocation(Location location);
}


