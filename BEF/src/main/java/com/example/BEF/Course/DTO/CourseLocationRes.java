package com.example.BEF.Course.DTO;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Location.DTO.LocationInfoRes;
import lombok.Data;

import java.util.List;

@Data
public class CourseLocationRes {
    //course 관련
    private Long courseNumber;
    private String courseName;
    private String description;

    List<LocationInfoRes> locationInfoResList;

    public CourseLocationRes(Course course, List<LocationInfoRes> locationInfoResList) {
        this.courseNumber = course.getCourseNumber();
        this.courseName = course.getCourseName();
        this. locationInfoResList = locationInfoResList;
    }
}
