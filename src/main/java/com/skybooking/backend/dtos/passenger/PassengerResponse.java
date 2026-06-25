package com.skybooking.backend.dtos.passenger;

import com.skybooking.backend.models.enums.DocumentType;

import java.time.LocalDate;

public record PassengerResponse(
        Long id,
        String firstName,
        String lastName,
        DocumentType documentType,
        String documentNumber,
        LocalDate birthDate,
        String nationality,
        String gender,
        String frequentFlyerNumber,
        Integer milesBalance
) {
}
