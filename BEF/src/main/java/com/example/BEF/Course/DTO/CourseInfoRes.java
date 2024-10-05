package com.example.BEF.Course.DTO;

import lombok.Data;

@Data
public class CourseInfoRes {
    private Long courseNumber;
    private String courseName;

    public CourseInfoRes(Long courseNumber, String courseName) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
    }
}
