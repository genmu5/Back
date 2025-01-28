package com.example.BEF.TripType;

import com.example.BEF.User.Domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_trip_type")
@NoArgsConstructor
public class UserTripType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userTripTypeNumber;

    @ManyToOne
    @JoinColumn(name = "user_number")

    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_type_number")

    private TripTypeEntity tripType;

    @Builder
    private UserTripType(User user, TripTypeEntity tripType) {
        this.user = user;
        this.tripType = tripType;
    }

    public static UserTripType of(User user, TripTypeEntity tripType) {
        return UserTripType.builder()
                .user(user)
                .tripType(tripType)
                .build();
    }
}
