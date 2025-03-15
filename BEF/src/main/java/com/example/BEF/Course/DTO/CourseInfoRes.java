package com.example.BEF.Course.DTO;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.CourseDisability;
import com.example.BEF.Disability.Disability;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class CourseInfoRes {
    private Long courseNumber;
    private String courseName;
    private String area;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean mobility;
    private boolean blind;
    private boolean hear;
    private boolean family;

    @Builder
    private CourseInfoRes(Course course, List<Long> disabilities) {
        this.courseNumber = course.getCourseNumber();
        this.courseName = course.getCourseName();
        this.area = course.getArea().getAreaName();
        this.startDate = course.getStartDate();
        this.endDate = course.getEndDate();
        this.mobility = disabilities.contains(1L);
        this.blind = disabilities.contains(2L);
        this.hear = disabilities.contains(3L);
        this.family = disabilities.contains(4L);
    }

    public static CourseInfoRes of(Course course, List<CourseDisability> courseDisabilities) {
        List<Long> disabilities = courseDisabilities.stream()
                .map(CourseDisability::getDisability)
                .map(Disability::getDisabilityNumber)
                .sorted()
                .toList();

        return new CourseInfoRes(course, disabilities);
    }
}
