package com.skybooking.backend.dtos.miles;

public record MilesBalanceResponse(
        Long passengerId,
        Integer currentBalance,
        String tierName,
        String nextTierName,
        Integer milesForNextTier,
        Double progressPercentage
) {
}
