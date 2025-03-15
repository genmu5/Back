package com.example.BEF.TripType;

import com.example.BEF.Disability.UserDisability;
import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTripTypeRepository extends JpaRepository<UserTripType, Long> {
    boolean existsByUserAndTripType(User user, TripType tripType);
    List<UserTripType> findAllByUser(User user);
}
