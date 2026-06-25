package com.skybooking.backend.dtos.miles;

import java.util.List;

public record MilesExpiringResponse(
        Long passengerId,
        List<MilesTransactionResponse> expiringTransactions,
        Integer totalExpiringSoon
) {
}
