package com.skybooking.backend.dtos.client;

public record UpdateProfileRequest(
        String firstName,
        String lastName,
        String phone,
        String avatar
) {
}
