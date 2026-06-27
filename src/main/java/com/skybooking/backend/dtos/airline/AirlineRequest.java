package com.skybooking.backend.dtos.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AirlineRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Pattern(regexp = "^[A-Z0-9]{2}$") String iataCode,
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$") String icaoCode,
        @NotBlank @Size(max = 100) String country,
        @Size(max = 255) String website,
        @Size(max = 255) String logoUrl
) {
}
