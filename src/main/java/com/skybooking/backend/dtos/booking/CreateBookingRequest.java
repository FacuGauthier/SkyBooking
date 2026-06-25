package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.dtos.passenger.PassengerRequest;
import com.skybooking.backend.models.enums.LuggageType;
import com.skybooking.backend.models.enums.TravelClass;

import java.util.List;

public record CreateBookingRequest(
        Long flightId,
        TravelClass travelClass,
        List<PassengerRequest> passengerRequest,
        List<String> seats,
        List<LuggageItemRequest> luggage
) {
    public record LuggageItemRequest(
            LuggageType type,
            Integer quantity
    ){
    }
}
