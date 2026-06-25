package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.dtos.flight.FlightSearchResponse;
import com.skybooking.backend.dtos.luggage.LuggageSummaryResponse;
import com.skybooking.backend.models.enums.BookingStatus;
import com.skybooking.backend.models.enums.DocumentType;
import com.skybooking.backend.models.enums.TravelClass;

import java.time.LocalDateTime;
import java.util.List;

public record BookingConfirmationResponse(
        Long bookingId,
        String bookingCode,
        BookingStatus status,
        LocalDateTime bookingDate,
        Double totalPrice,
        FlightSearchResponse flight,
        List<BookingPassengerItem> passengers,
        List<LuggageSummaryResponse> luggage
) {
    public record BookingPassengerItem(
            Long passengerId,
            String firstName,
            String lastName,
            DocumentType documentType,
            String documentNumber,
            String seatNumber,
            TravelClass travelClass
    ){
    }
}
