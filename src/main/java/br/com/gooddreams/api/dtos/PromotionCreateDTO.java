package br.com.gooddreams.api.dtos;

import java.time.Instant;

public record PromotionCreateDTO(
        float discountPercentage,
        Instant startDate,
        Instant endDate,
        boolean isActive,
        Long productVariationId
) {
}
