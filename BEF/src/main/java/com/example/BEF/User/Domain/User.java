package com.example.BEF.User.Domain;

import com.example.BEF.Course.Domain.Course;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@Getter @Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNumber;      // 유저 번호

    @OneToMany(mappedBy = "user")
    List<Course> courseList;

    @OneToMany(mappedBy = "uuidUser", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Course> courseListByUuid;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "user_name")
    private String userName;      // 유저 이름

    @Column(name = "gender")
    private String gender;        // 성별

    @Column(name = "age")
    private Long age;             // 나이

    @Column(name = "senior")
    private Boolean senior;       // 노약자

    @Column(name = "wheelchair")
    private Boolean wheelchair;   // 휠체어

    @Column(name = "blind_handicap")
    private Boolean blindHandicap; // 시각 장애

    @Column(name = "hearing_handicap")
    private Boolean hearingHandicap; // 청각 장애

    @Column(name = "infants_family")
    private Boolean infantsFamily; // 영유아 가족

    @Column(name = "forest")
    private Boolean forest;

    @Column(name = "ocean")
    private Boolean ocean;

    @Column(name = "culture")
    private Boolean culture;

    @Column(name = "outside")
    private Boolean outside;

    public User(String userName, String gender, Long age, Boolean senior, Boolean wheelchair,
                Boolean blindHandicap, Boolean hearingHandicap, Boolean infantsFamily, String uuid) {
        this.userName = userName;
        this.gender = gender;
        this.age = age;
        this.senior = senior;
        this.wheelchair = wheelchair;
        this.blindHandicap = blindHandicap;
        this.hearingHandicap = hearingHandicap;
        this.infantsFamily = infantsFamily;
        this.uuid = uuid;
    }
}