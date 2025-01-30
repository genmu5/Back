package com.example.BEF.Course.Repository;

import com.example.BEF.Course.Domain.Saved;
import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedRepository extends JpaRepository<Saved, Long> {
    List<Saved> findAllByUser(User user);
}
