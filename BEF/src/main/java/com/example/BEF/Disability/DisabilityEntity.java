package com.example.BEF.Disability;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class DisabilityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disability_number")
    private Long disabilityNumber;

    private String name;
}
