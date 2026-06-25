package com.skybooking.backend.dtos.passenger;

import com.skybooking.backend.models.enums.DocumentType;

import java.time.LocalDate;

public record PassengerRequest(
        String firstName,
        String lastName,
        DocumentType documentType,
        String documentNumber,
        LocalDate birthDate,
        String nationality,
        String gender
) {
}
