package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.FlightStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FlightRequest(
        @NotBlank @Size(max = 20) String flightNumber,
        @NotNull @Positive Long originAirportId,
        @NotNull @Positive Long destinationAirportId,
        @NotNull LocalDateTime departureTime,
        @NotNull LocalDateTime arrivalTime,
        @NotNull FlightStatus status,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) Double basePrice,
        @NotNull @Positive Long planeId,
        @Min(0) Integer stops
) {
}
