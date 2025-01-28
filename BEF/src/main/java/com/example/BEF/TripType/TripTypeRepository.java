package com.example.BEF.TripType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripTypeRepository extends JpaRepository<TripTypeEntity, Long> {
    boolean existsByName(String name);
    TripTypeEntity findByName(String name);
}
