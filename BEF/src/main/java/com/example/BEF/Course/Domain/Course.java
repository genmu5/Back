package com.example.BEF.Course.Domain;

import com.example.BEF.User.Domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@NoArgsConstructor
@Getter @Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_number")
    private Long courseNumber;

    @Column(name = "course_name")
    private String courseName;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    public Course(User user, String courseName) {
        this.user = user;
        this.courseName = courseName;
    }
}
