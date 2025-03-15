package com.example.BEF.Course.DTO;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.CourseDisability;
import com.example.BEF.Location.DTO.LocationInfoRes;
import lombok.Data;

import java.util.List;

@Data
public class CourseLocationRes {
    CourseInfoRes courseInfo;
    List<LocationInfoRes> locationInfoResList;

    public CourseLocationRes(Course course, List<CourseDisability> courseDisabilities, List<LocationInfoRes> locationInfoResList) {
        this.courseInfo = CourseInfoRes.of(course, courseDisabilities);
        this. locationInfoResList = locationInfoResList;
    }
}
