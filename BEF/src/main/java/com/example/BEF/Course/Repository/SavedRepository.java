package com.example.BEF.Course.Repository;

import com.example.BEF.Course.Domain.Saved;
import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedRepository extends JpaRepository<Saved, Long> {
    List<Saved> findAllByUser(User user);
    Optional<Saved> findByUserAndLocation(User user, Location location);
}
