package com.example.BEF.TripType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripTypeRepository extends JpaRepository<TripType, Long> {
    boolean existsByTripTypeNumber(Long number);
    TripType findByName(String name);
    TripType findTripTypeEntityByTripTypeNumber(Long number);

}
