package com.skybooking.backend.models;

import com.skybooking.backend.models.enums.DocumentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    private String nationality;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String gender;

    @Column(unique = true)
    private String frequentFlyerNumber;

    @Column(nullable = false)
    private Integer milesBalance = 0;

    @OneToMany(mappedBy = "passenger")
    private List<Passage> passages;
}
