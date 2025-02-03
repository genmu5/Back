package com.example.BEF.Course.Domain;

import com.example.BEF.Disability.Disability;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class CourseDisability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_number")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "disability_ number")
    private Disability disability;

    @Builder
    public CourseDisability(Course course, Disability disability) {
        this.course = course;
        this.disability = disability;
    }
}
