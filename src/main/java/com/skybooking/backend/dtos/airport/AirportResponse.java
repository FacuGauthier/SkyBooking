package com.skybooking.backend.dtos.airport;

public record AirportResponse(
        Long id,
        String name,
        String iataCode,
        String icaoCode,
        String city,
        String country,
        String timezone,
        Double latitude,
        Double longitude
) {
}
