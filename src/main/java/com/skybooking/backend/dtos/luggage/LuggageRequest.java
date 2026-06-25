package com.skybooking.backend.dtos.luggage;

import com.skybooking.backend.models.enums.LuggageType;

public record LuggageRequest(
        LuggageType type,
        Double weightKg,
        Double heightCm,
        Double widthCm,
        Double lengthCm,
        String description,
        /*
         * No exist en la entity.
         * El service debe create N registers de Luggage.
         */
        Integer quantity
) {
}
