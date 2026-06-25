package com.skybooking.backend.dtos.flight;

public record FlightOccupancyResponse(
        Long flightId,
        String flightNumber,
        Integer totalSeats,
        Integer occupiedSeats,
        Integer availableSeats,
        Integer economyOccupied,
        Integer economyTotal,
        Integer businessOccupied,
        Integer businessTotal,
        Double occupancyPercentage
) {
}
