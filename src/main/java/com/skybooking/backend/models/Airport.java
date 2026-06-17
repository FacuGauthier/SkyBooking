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
@Table(name = "airports")
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false, length = 2)
    private String iataCode;

    @Column(unique = true, nullable = false, length = 3)
    private String icaoCode;

    private String city;

    private String country;

    private String timezone;

    private Double latitude;
    private Double longitude;
}
