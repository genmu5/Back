package com.example.BEF.Course.Domain;

import com.example.BEF.Area.Domain.Area;
import com.example.BEF.User.Domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "course")
@NoArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_number")
    private Long courseNumber;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "period")
    private Long period;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "gps_x")
    private Double gpsX;

    @Column(name = "gps_y")
    private Double gpsY;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "area_number")
    private Area area;

    @OneToMany(mappedBy = "course")
    private List<CourseDisability> courseDisabilities;

    @OneToMany(mappedBy = "course")
    private List<UserCourse> userCourses;

    @Builder
    public Course(String courseName, Long period, Double gpsX, Double gpsY, Area area, User user) {
        this.courseName = courseName;
        this.user = user;
        this.period = period;
        this.gpsX = gpsX;
        this.gpsY = gpsY;
        this.area = area;
    }
}
