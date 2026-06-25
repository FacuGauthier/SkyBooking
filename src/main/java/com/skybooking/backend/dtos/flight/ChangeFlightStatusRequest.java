package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.FlightStatus;

public record ChangeFlightStatusRequest(
        FlightStatus status
) {
}
