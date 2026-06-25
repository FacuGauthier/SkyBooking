package com.skybooking.backend.dtos.luggage;

import com.skybooking.backend.models.enums.LuggageType;

public record LuggageSummaryResponse(
        Long id,
        LuggageType type,
        String description,
        Double additionalCost
) {
}
