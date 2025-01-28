package com.example.BEF.TripType;

import jakarta.persistence.*;

@Entity
public class TripTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_type_number")
    private Long tripTypeNumber;

    private TripType name;
}
