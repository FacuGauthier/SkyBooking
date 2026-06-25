package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.models.enums.DocumentType;

import java.time.LocalDate;

public record UpdatePassengerRequest(
        String firstName,
        String lastName,
        DocumentType documentType,
        String documentNumber,
        LocalDate birthDate,
        String nationality,
        String gender
) {
}
