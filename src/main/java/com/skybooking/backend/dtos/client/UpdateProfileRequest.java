package com.skybooking.backend.dtos.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank @Size(max = 80) String firstName,
        @NotBlank @Size(max = 80) String lastName,
        @Pattern(regexp = "^$|^[+0-9][0-9\\s()\\-]{6,24}$") String phone,
        @Size(max = 255) String avatar
) {
}
