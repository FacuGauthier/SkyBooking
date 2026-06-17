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

    private LuggageType type;

    private Double weightKg;
    private Double heightCm;
    private Double widthCm;
    private Double lengthCm;

    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal additionalCost;

    @ManyToOne
    @JoinColumn(name = "passage_id")
    private Passage passage;
}
