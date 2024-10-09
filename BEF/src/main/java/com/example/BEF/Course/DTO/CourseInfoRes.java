package com.example.BEF.Course.DTO;

import lombok.Data;

@Data
public class CourseInfoRes {
    private Long courseNumber;
    private String courseName;
    private String description;

    public CourseInfoRes(Long courseNumber, String courseName, String description) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.description = description;
    }
}
