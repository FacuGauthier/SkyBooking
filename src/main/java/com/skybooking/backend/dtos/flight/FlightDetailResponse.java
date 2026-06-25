package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.dtos.airline.AirlineResponse;
import com.skybooking.backend.dtos.airport.AirportResponse;
import com.skybooking.backend.dtos.plane.PlaneResponse;
import com.skybooking.backend.models.enums.FlightStatus;

import java.time.LocalDateTime;

public record FlightDetailResponse(
        Long id,
        String flightNumber,
        AirportResponse origin,
        AirportResponse destination,
        LocalDateTime departure,
        LocalDateTime arrival,
        String duration,
        FlightStatus status,
        Double economyPrice,
        Double businessPrice,
        Integer availableEconomySeats,
        Integer availableBusinessSeats,
        PlaneResponse plane,
        AirlineResponse airline,
        Integer stops
) {
}
