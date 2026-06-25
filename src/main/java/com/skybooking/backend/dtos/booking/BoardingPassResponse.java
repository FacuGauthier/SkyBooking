package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.models.enums.TravelClass;

import java.time.LocalDateTime;

public record BoardingPassResponse(
        String bookingCode,
        String passengerFullName,
        String documentNumber,
        String flightNumber,
        String originCode,
        String originCity,
        String destinationCode,
        String destinationCity,
        LocalDateTime departure,
        LocalDateTime arrival,
        String seatNumber,
        TravelClass travelClass,
        String airline,
        String gate,
        LocalDateTime boardingTime,
        String barcodeData
) {
}
