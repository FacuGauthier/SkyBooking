package com.skybooking.backend.dtos.auth;

public record AuthResponse(
        String token,
        String id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String role,
        String avatar,
        String milesBalance,
        String frequencyFlyerNumber
) {
}
