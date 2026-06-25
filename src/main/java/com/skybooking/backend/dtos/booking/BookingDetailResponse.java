package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.dtos.flight.FlightDetailResponse;
import com.skybooking.backend.dtos.flight.FlightSearchResponse;
import com.skybooking.backend.dtos.luggage.LuggageResponse;
import com.skybooking.backend.models.enums.BookingStatus;
import com.skybooking.backend.models.enums.TravelClass;

import java.time.LocalDateTime;
import java.util.List;

public record BookingDetailResponse(
        Long bookingId,
        String bookingCode,
        BookingStatus status,
        LocalDateTime bookingDate,
        Double totalPrice,
        FlightDetailResponse flight,
        TravelClass travelClass,
        List<BookingConfirmationResponse.BookingPassengerItem> passengers,
        List<LuggageResponse> luggage,
        PriceBreakdownResponse priceBreakdown
) {
    public record PriceBreakdownResponse(
            Double flightPricePerPassenger,
            Integer passengerCount,
            Double flightSubtotal,
            Double luggageTotal,
            Double total
    ){
    }
}
