package com.skybooking.backend.models;

import com.skybooking.backend.models.enums.BookingStatus;
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
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bookingCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private LocalDateTime bookingDate;

    @Column(nullable = false, precision =  19, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "booking")
    private List<Passage> passages;



    @PrePersist
    public void prePersist() {
        if (bookingDate == null) bookingDate = LocalDateTime.now();
    }
}
