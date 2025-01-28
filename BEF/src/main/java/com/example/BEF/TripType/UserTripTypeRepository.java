package com.example.BEF.TripType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTripTypeRepository extends JpaRepository<UserTripType, Long> {
}
