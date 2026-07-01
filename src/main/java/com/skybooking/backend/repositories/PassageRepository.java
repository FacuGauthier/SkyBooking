package com.skybooking.backend.repositories;

import com.skybooking.backend.models.Passage;
import com.skybooking.backend.models.enums.BookingStatus;
import com.skybooking.backend.models.enums.TravelClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassageRepository extends JpaRepository<Passage, Long> {
    @Query("SELECT p.seatNumber FROM Passage p " +
            "WHERE p.flight.id = :flightId " +
            "AND p.booking.status <> com.skybooking.backend.models.enums.BookingStatus.CANCELLED")
    List<String> findOccupiedSeatsByFlightId(@Param("flightId") Long flightId);
    int countByFlight_IdAndTravelClassAndBooking_StatusNot(Long flightId, TravelClass travelClass, BookingStatus bookingStatus);
}
