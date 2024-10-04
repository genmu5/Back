package com.example.BEF.Course.Service;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.UserCourse;
import com.example.BEF.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    UserCourse findUserCourseByCourseAndLocation(Course course, Location location);

    List<UserCourse> findUserCoursesByCourse(Course course);
}
