package com.skybooking.backend.dtos.client;

import com.skybooking.backend.models.enums.Role;

public record ClientProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        String avatar,
        Integer milesBalance,
        String frequencyFlyerNumber,
        Boolean active
) {
}
