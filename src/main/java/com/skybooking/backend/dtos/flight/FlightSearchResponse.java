package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.dtos.airline.AirlineSummaryResponse;
import com.skybooking.backend.dtos.airport.AirportSummaryResponse;
import com.skybooking.backend.models.enums.FlightStatus;

import java.time.LocalDateTime;

public record FlightSearchResponse(
        Long id,
        String flightNumber,
        AirportSummaryResponse origin,
        AirportSummaryResponse destination,
        LocalDateTime departure,
        LocalDateTime arrival,
        String duration,
        FlightStatus status,
        Double economyPrice,
        Double businessPrice,
        Integer availableSeats,
        String plane, // 'model' like string
        AirlineSummaryResponse airline,
        Integer stops
) {
}
