package com.example.BEF.Disability;

import jakarta.persistence.*;

@Entity
public class DisabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disability_number")
    private Long disabilityNumber;

    private Disability name;
}
