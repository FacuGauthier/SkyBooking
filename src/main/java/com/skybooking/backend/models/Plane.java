package com.skybooking.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planes")
public class Plane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registration;

    private String model;

    private Integer capacity;

    private Integer businessSeats;
    private Integer economySeats;

    private Integer manufactureYear;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;
}
