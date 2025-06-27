package br.com.gooddreams.api.dtos;

import java.time.Instant;

public record PromotionUpdateDTO(
        Long id,
        float discountPercentage,
        Instant startDate,
        Instant endDate,
        boolean isActive
) {
}
