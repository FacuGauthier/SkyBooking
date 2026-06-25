package com.skybooking.backend.dtos.miles;

import com.skybooking.backend.models.enums.MilesTransactionType;

import java.time.LocalDateTime;

public record MilesTransactionResponse(
        Long id,
        MilesTransactionType type,
        Integer amount,
        String description,
        LocalDateTime transactionDate,
        LocalDateTime expiresAt
) {
}
