package com.skybooking.backend.dtos.airline;

public record AirlineResponse(
        Long id,
        String name,
        String iataCode,
        String icaoCode,
        String country,
        String website,
        String logoUrl
) {
}
