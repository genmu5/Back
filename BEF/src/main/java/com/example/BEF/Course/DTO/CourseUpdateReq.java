package com.example.BEF.Course.DTO;

import lombok.Getter;

import java.util.List;

@Getter
public class CourseUpdateReq {
    private Long courseNumber;
    private Long userNumber;
    private List<List<Long>> courseLocByDay;
}
