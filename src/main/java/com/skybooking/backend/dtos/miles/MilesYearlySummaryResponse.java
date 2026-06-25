package com.skybooking.backend.dtos.miles;

public record MilesYearlySummaryResponse(
        Integer year,
        Integer totalEarned,
        Integer totalRedeemed,
        Integer totalExpired
) {
}
