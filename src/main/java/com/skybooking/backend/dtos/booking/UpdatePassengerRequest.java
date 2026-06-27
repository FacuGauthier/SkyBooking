package com.skybooking.backend.dtos.booking;

import com.skybooking.backend.models.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdatePassengerRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotNull DocumentType documentType,
        @NotBlank @Size(max = 40) String documentNumber,
        @NotNull @Past LocalDate birthDate,
        @Size(max = 80) String nationality,
        @Size(max = 30) String gender
) {
}
