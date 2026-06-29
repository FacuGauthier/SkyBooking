package com.skybooking.backend.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(
        @NotBlank(message = "La contraseña actual es obligatoria") String currentPassword,
        @NotBlank(message = "La nueva contraseña es obligatoria") @Size(min = 8, max = 72, message = "La nueva contraseña debe tener al menos 6 caracteres") String newPassword
) {
}
