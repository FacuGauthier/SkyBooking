package com.skybooking.backend.dtos.airline;

public record AirlineRequest(
        String name,
        String iataCode,
        String icaoCode,
        String country,
        String website,
        String logoUrl
) {
}
