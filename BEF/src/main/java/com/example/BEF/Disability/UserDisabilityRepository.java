package com.example.BEF.Disability;

import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDisabilityRepository extends JpaRepository<UserDisability, Long> {
    boolean existsByUserAndDisability(User user, Disability disability);
    List<UserDisability> findAllByUser(User user);
}
