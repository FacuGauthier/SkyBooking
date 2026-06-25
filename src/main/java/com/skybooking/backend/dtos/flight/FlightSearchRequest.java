package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.TravelClass;

import java.time.LocalDate;

public record FlightSearchRequest(
        String originCode,
        String destinationCode,
        LocalDate departureDate,
        TravelClass travelClass,
        Double maxPrice,
        String airlineName
) {
}
