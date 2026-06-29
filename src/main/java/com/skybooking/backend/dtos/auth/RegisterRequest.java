package com.skybooking.backend.dtos.auth;

import com.skybooking.backend.models.enums.DocumentType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Email String email,
        @Pattern(regexp = "^$|^[+0-9][0-9\\s()\\-]{6,24}$") String phone,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotNull DocumentType documentType,
        @NotBlank @Size(min = 6, max = 20) @Pattern(regexp = "^[A-Za-z0-9]+$") String documentNumber,
        @NotNull @Past LocalDate birthDate
) {
}
