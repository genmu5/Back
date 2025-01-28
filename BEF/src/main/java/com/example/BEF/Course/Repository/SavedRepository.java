package com.example.BEF.Course.Repository;

import com.example.BEF.Course.Domain.Saved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedRepository extends JpaRepository<Saved, Long> {
}
