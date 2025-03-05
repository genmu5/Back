package com.example.BEF.Course.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class CourseLocRes {
    private Long courseNumber;
    private String courseName;
    private List<List<SimpleLoc>> contentIdList;


    private CourseLocRes(Long courseNumber, String courseName, List<List<SimpleLoc>> contentIdList) {
        this.courseNumber = courseNumber;
        this.courseName = courseName;
        this.contentIdList = contentIdList;
    }

    @Builder
    public static CourseLocRes of(Long courseNumber, String courseName, List<List<SimpleLoc>> contentIdList) {
        return new CourseLocRes(courseNumber, courseName, contentIdList);
    }
}
