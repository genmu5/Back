package com.example.BEF.Disability;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.Course.Domain.CourseDisability;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Disability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disability_number")
    private Long disabilityNumber;

    private String name;


    @OneToMany(mappedBy = "disability")
    private List<CourseDisability> courseDisabilities;
}
