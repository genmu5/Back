package com.example.BEF.User.Domain;

import com.example.BEF.Course.Domain.Course;
import com.example.BEF.TripType.*;
import com.example.BEF.Disability.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNumber;      // 유저 번호

    @Column(name = "user_name")
    private String userName;

    @Column(name = "gender")
    private String gender;        // 성별

    @Column(name = "birth")
    private LocalDate birth;             // 나이

    @OneToMany(mappedBy = "user")
    List<Course> courseList;

    @OneToMany(mappedBy = "user")
    List<UserDisability> disabilities;

    @OneToMany(mappedBy = "user")
    List<UserTripType> tripTypes;

    public User(String userName, String gender, LocalDate birth) {
        this.userName = userName;
        this.gender = gender;
        this.birth = birth;
    }
}