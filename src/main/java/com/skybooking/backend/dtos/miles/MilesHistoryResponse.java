package com.skybooking.backend.dtos.miles;

import java.util.List;

public record MilesHistoryResponse(
        Long passengerId,
        List<MilesTransactionResponse> transactions
) {
}
