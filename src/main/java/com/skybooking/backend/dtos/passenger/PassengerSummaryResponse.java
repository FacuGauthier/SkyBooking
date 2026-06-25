package com.skybooking.backend.dtos.passenger;

import com.skybooking.backend.models.enums.DocumentType;

public record PassengerSummaryResponse(
        Long id,
        String firstName,
        String lastName,
        DocumentType documentType,
        String documentNumber
) {
}
