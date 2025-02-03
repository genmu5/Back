package com.example.BEF.TripType;

import com.example.BEF.Course.Domain.Course;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TripType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_type_number")
    private Long tripTypeNumber;

    private String name;

    @ManyToOne
    @JoinColumn(name = "course_number")
    private Course course;
}
