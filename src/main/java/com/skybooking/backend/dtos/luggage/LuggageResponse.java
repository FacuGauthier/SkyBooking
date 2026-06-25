package com.skybooking.backend.dtos.luggage;

import com.skybooking.backend.models.enums.LuggageType;

public record LuggageResponse(
        Long id,
        LuggageType type,
        Double weightKg,
        Double heightCm,
        Double widthCm,
        Double lengthCm,
        String description,
        Double additionalCosts,
        Long passengerId
) {
}
