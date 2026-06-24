package com.skybooking.backend.dtos.airline;

public record AirlineSummaryResponse(
        Long id,
        String name,
        String iataCode,
        String logoUrl
) {
}
