package com.example.BEF.Course.Domain;

import com.example.BEF.TripType.TripType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class CourseTripType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_number")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "trip_type_number")
    private TripType tripType;

    @Builder
    public CourseTripType(Course course, TripType tripType) {
        this.course = course;
        this.tripType = tripType;
    }
}

