package com.skybooking.backend.dtos.plane;

public record PlaneRequest(
        String registration,
        String model,
        Integer businessSeats,
        Integer economySeats,
        Integer manufactureYear,
        Long airlineId
) {
}
