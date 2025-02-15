package com.example.BEF.Course.Domain;

import com.example.BEF.Location.Domain.Location;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_course")
@NoArgsConstructor
@Getter
public class UserCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_course_number")
    private Long userCourseNumber;

    @Column(name = "day")
    private Long day;

    @ManyToOne
    @JoinColumn(name = "course_number")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Location location;

    @Builder
    public UserCourse(Long day, Course course, Location location) {
        this.day = day;
        this.course = course;
        this.location = location;
    }
}
