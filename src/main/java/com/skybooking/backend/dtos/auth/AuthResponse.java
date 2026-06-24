package com.skybooking.backend.dtos.auth;

import com.skybooking.backend.models.enums.Role;

public record AuthResponse(
        String token,
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        String avatar,
        String milesBalance,
        String frequencyFlyerNumber
) {
}
