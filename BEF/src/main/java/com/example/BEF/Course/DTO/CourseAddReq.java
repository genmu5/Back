package com.example.BEF.Course.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CourseAddReq {
    List<Long> contentIdList;
}
