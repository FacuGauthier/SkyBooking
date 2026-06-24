package com.skybooking.backend.dtos.client;

import com.skybooking.backend.models.enums.Role;

public record ClientSummaryResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Role role,
        Boolean active
) {
}
