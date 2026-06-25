package com.skybooking.backend.dtos.flight;

import com.skybooking.backend.models.enums.DocumentType;
import com.skybooking.backend.models.enums.TravelClass;

import java.time.LocalDateTime;
import java.util.List;

public record FlightManifestResponse(
        Long flightId,
        String flightNumber,
        LocalDateTime departure,
        List<ManifestPassengerItem> passengers
) {
    public record ManifestPassengerItem(
            Long passengerId,
            String firstName,
            String lastName,
            DocumentType documentType,
            String documentNumber,
            String seatNumber,
            TravelClass travelClass,
            Integer luggageCount
    ){
    }
}
