package com.skybooking.backend.models;

import com.skybooking.backend.models.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(nullable = false)
    private String flightNumber;

    @ManyToOne
    @JoinColumn(name = "origin_airport_id")
    private Airport originAirport;

    @ManyToOne
    @JoinColumn(name = "destination_airport_id")
    private Airport destinationAirport;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlightStatus status;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    @ManyToOne
    @JoinColumn(name = "plane_id")
    private Plane plane;

    @Column(nullable = false)
    private Integer stops = 0;

    @OneToMany(mappedBy = "flight")
    private List<Passage> passages;


    public Airline getAirline() {
        return plane != null ? plane.getAirline() : null;
    }
}
