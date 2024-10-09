package com.example.BEF.Course.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourseLocRes {
    private Long courseNumber;
    private String courseName;
    private List<Long> contentIdList;

    public CourseLocRes(Long courseNumber, String courseName, List<Long> contentIdList) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.contentIdList = contentIdList;
    }
}
