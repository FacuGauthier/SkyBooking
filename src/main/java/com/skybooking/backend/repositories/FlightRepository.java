package com.skybooking.backend.repositories;

import com.skybooking.backend.models.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT f FROM Flight f " +
            "WHERE f.status <> com.skybooking.backend.models.enums.FlightStatus.CANCELLED " +
            "AND (:originCode IS NULL OR f.originAirport.iataCode = :originCode) " +
            "AND (:destinationCode IS NULL OR f.destinationAirport.iataCode = :destinationCode) " +
            "AND (:startDateTime IS NULL OR f.departureTime >= :startDateTime) " +
            "AND (:endDateTime IS NULL OR f.departureTime <= :endDateTime) " +
            "AND (:airlineName IS NULL OR LOWER(f.plane.airline.name) LIKE LOWER(CONCAT('%', :airlineName, '%'))) " +
            "AND (:maxPrice IS NULL OR " +
            "     (:travelClass = 'BUSINESS' AND f.businessPrice <= :maxPrice) OR " +
            "     ((:travelClass IS NULL OR :travelClass = 'ECONOMY') AND f.economyPrice <= :maxPrice))")
    List<Flight> searchFlightsDynamic(
            @Param("originCode") String originCode,
            @Param("destinationCode") String destinationCode,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("airlineName") String airlineName,
            @Param("maxPrice") Double maxPrice,
            @Param("travelClass") String travelClass
    );
    @Query("SELECT COUNT(f) > 0 FROM Flight f WHERE f.plane.id = :planeId " +
            "AND f.departureTime < :arrivalTime AND f.arrivalTime > :departureTime")
    boolean existsOverlappingFlight(@Param("planeId") Long planeId,
                                    @Param("departureTime") LocalDateTime departureTime,
                                    @Param("arrivalTime") LocalDateTime arrivalTime);
    @Query("SELECT COUNT(f) > 0 FROM Flight f WHERE f.plane.id = :planeId AND f.id <> :flightId " +
            "AND f.departureTime < :arrivalTime AND f.arrivalTime > :departureTime")
    boolean existsOverlappingFlightExcluding(@Param("planeId") Long planeId,
                                             @Param("departureTime") LocalDateTime departureTime,
                                             @Param("arrivalTime") LocalDateTime arrivalTime,
                                             @Param("flightId") Long flightId);
}
