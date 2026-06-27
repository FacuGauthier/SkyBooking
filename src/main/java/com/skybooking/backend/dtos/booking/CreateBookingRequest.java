package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.dtos.passenger.PassengerRequest;
import com.skybooking.backend.models.enums.LuggageType;
import com.skybooking.backend.models.enums.TravelClass;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateBookingRequest(
        @NotNull @Positive Long flightId,
        @NotNull TravelClass travelClass,
        @NotEmpty List<@Valid PassengerRequest> passengerRequest,
        @NotEmpty List<@NotBlank @Size(max = 4) String> seats,
        List<@Valid LuggageItemRequest> luggage
) {
    public record LuggageItemRequest(
            @NotNull LuggageType type,
            @NotNull @Positive Integer quantity
    ){
    }
}
