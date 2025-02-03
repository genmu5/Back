package com.example.BEF.Disability;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisabilityRepository extends JpaRepository<Disability, Long> {
    boolean existsByDisabilityNumber(Long number);
    Disability findByName(String name);
    Disability findDisabilityByDisabilityNumber(Long number);
}
