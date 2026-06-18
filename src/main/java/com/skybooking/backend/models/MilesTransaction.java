package com.skybooking.backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "miles_transactions")
public class MilesTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "passage_id")
    private Passage passage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilesTransactionType type;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    private LocalDate expiresAt;

    private String description;
}
