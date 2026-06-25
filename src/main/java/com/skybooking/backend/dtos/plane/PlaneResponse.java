package com.skybooking.backend.dtos.plane;

public record PlaneResponse(
        Long id,
        String registration,
        String model,
        Integer businessSeats,
        Integer economySeats,
        Integer totalCapacity,
        Integer manufactureYear,
        Long airlineId,
        String airlineName
) {
}
