package com.skybooking.backend.dtos.plane;

public record PlaneSummaryResponse(
        Long id,
        String model,
        String registration,
        Integer totalCapacity
) {
}
