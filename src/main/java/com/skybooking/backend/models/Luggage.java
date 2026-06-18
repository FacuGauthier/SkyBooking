package com.skybooking.backend.models;

import com.skybooking.backend.models.enums.LuggageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "luggage")
public class Luggage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LuggageType type;

    private Double weightKg;
    private Double heightCm;
    private Double widthCm;
    private Double lengthCm;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal additionalCost;

    @ManyToOne
    @JoinColumn(name = "passage_id", nullable = false)
    private Passage passage;
}
