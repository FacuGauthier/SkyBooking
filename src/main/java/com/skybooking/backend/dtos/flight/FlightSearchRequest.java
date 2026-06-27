package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.TravelClass;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record FlightSearchRequest(
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String originCode,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String destinationCode,
        @NotNull LocalDate departureDate,
        TravelClass travelClass,
        @DecimalMin(value = "0.0", inclusive = false) Double maxPrice,
        @Size(max = 100) String airlineName
) {
}
