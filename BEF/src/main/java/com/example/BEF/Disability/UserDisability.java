package com.example.BEF.Disability;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.User.Domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_disability")
@NoArgsConstructor
@Getter
public class UserDisability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDisabilityNumber;

    @ManyToOne
    @JoinColumn(name = "user_number")

    private User user;

    @ManyToOne
    @JoinColumn(name = "disability_number")
    private DisabilityEntity disability;

    @ManyToOne
    @JoinColumn(name = "course_number")
    private Course course;

    @Builder
    private UserDisability(User user, DisabilityEntity disability) {
        this.user = user;
        this.disability = disability;
    }

    public static UserDisability of(User user, DisabilityEntity disability) {
        return UserDisability.builder()
                .user(user)
                .disability(disability)
                .build();
    }
}
