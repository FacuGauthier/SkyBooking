package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.FlightStatus;

import java.time.LocalDateTime;

public record FlightRequest(
        String flightNumber,
        Long originAirportId,
        Long destinationAirportId,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        FlightStatus status,
        Double basePrice,
        Long planeId,
        Integer stops
) {
}
