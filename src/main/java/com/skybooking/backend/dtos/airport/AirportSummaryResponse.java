package com.skybooking.backend.dtos.airport;

public record AirportSummaryResponse(
        Long id,
        String iataCode,
        String city,
        String country
) {
}
