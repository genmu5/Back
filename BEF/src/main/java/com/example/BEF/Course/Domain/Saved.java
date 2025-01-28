package com.example.BEF.Course.Domain;

import com.example.BEF.Location.Domain.Location;
import com.example.BEF.User.Domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "saved")
@NoArgsConstructor
public class Saved {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saved_number")
    private Long savedNumber;

    @ManyToOne
    @JoinColumn(name = "user_number")
    private User user;

    @ManyToOne
    @JoinColumn(name = "location_number")
    private Location location;

    @Builder
    private Saved(User user, Location location) {
        this.user = user;
        this.location = location;
    }

    public static Saved of(User user, Location location) {
        return Saved.builder()
                .user(user)
                .location(location)
                .build();
    }
}
