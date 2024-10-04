package com.example.BEF.Course.Service;

import com.example.BEF.Course.DTO.CourseInfoRes;
import com.example.BEF.Course.Domain.Course;
import com.example.BEF.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<CourseInfoRes> findCourseNumbersAndCourseNamesByUser(User user);

    Course findCourseByCourseNumber(Long CourseNumber);
}
