package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.FlightStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeFlightStatusRequest(
        @NotNull FlightStatus status
) {
}
