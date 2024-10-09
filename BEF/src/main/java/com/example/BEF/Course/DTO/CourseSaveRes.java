package com.example.BEF.Course.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseSaveRes {
    private Long courseNumber;
    private String courseName;
    private Long contentId;

    public CourseSaveRes(Long courseNumber, String courseName, Long contentId) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.contentId = contentId;
    }
}
