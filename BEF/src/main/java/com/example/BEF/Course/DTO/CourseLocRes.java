package com.example.BEF.Course.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseLocRes {
    private Long courseNumber;
    private String courseName;
    private Long locNumber;

    public CourseLocRes(Long courseNumber, String courseName, Long locNumber) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.locNumber = locNumber;
    }
}
