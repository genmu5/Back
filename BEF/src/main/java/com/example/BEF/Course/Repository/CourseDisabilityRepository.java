package com.example.BEF.Course.Repository;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.CourseDisability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseDisabilityRepository extends JpaRepository<CourseDisability, Long> {
    List<CourseDisability> findAllByCourse(Course course);
}
