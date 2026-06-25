package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.dtos.flight.FlightSearchResponse;
import com.skybooking.backend.models.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public record BookingSummaryResponse(
        Long bookingId,
        String bookingCode,
        BookingStatus status,
        LocalDateTime bookingDate,
        Double totalPrice,
        FlightSearchResponse flight,
        Integer passengerCount,
        List<BookingConfirmationResponse.BookingPassengerItem> passengers
) {
}
