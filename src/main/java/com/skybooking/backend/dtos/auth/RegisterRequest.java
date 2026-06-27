package com.skybooking.backend.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @NotBlank @Email String email,
        @Pattern(regexp = "^$|^[+0-9][0-9\\s()\\-]{6,24}$") String phone,
        @NotBlank @Size(min = 8, max = 72) String password
) {
}
