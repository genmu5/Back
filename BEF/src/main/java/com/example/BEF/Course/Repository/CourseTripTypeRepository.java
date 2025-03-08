package com.example.BEF.Course.Repository;

import com.example.BEF.Course.Domain.CourseTripType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseTripTypeRepository extends JpaRepository<CourseTripType, Long> {
}
