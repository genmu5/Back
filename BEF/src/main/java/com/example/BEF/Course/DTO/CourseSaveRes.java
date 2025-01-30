package com.example.BEF.Course.DTO;

import lombok.Data;

@Data
public class CourseSaveRes {
    private Long courseNumber;
    private String courseName;
    private Long contentId;

    public CourseSaveRes(Long contentId) {
        this.contentId = contentId;
    }
}
