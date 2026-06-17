package com.skybooking.backend.models;

import com.skybooking.backend.models.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String flightNumber;

    private Airport originAirport;
    private Airport destinationAirport;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private FlightStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;
}
