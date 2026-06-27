package com.skybooking.backend.dtos.luggage;

import com.skybooking.backend.models.enums.LuggageType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LuggageRequest(
        @NotNull LuggageType type,
        @DecimalMin(value = "0.0", inclusive = false) Double weightKg,
        @DecimalMin(value = "0.0", inclusive = false) Double heightCm,
        @DecimalMin(value = "0.0", inclusive = false) Double widthCm,
        @DecimalMin(value = "0.0", inclusive = false) Double lengthCm,
        @Size(max = 255) String description,
        /*
         * No exist en la entity.
         * El service debe create N registers de Luggage.
         */
        @NotNull @Positive Integer quantity
) {
}
