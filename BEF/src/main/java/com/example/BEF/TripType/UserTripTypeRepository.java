package com.example.BEF.TripType;

import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTripTypeRepository extends JpaRepository<UserTripType, Long> {
    boolean findByUserAndTripType(User user, TripTypeEntity tripType);

}
