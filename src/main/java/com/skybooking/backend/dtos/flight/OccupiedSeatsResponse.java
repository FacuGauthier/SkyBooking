package com.skybooking.backend.dtos.flight;

import java.util.List;

public record OccupiedSeatsResponse(
        Long flightId,
        List<String> occupiedSeats // ["1A", "3C", "7F"]
) {
}
