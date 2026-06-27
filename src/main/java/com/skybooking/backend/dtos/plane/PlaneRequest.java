package com.skybooking.backend.dtos.plane;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record PlaneRequest(
        @NotBlank @Size(max = 20) String registration,
        @NotBlank @Size(max = 80) String model,
        @NotNull @PositiveOrZero Integer businessSeats,
        @NotNull @PositiveOrZero Integer economySeats,
        @NotNull @Min(1903) @Max(2100) Integer manufactureYear,
        @NotNull @Positive Long airlineId
) {
}
