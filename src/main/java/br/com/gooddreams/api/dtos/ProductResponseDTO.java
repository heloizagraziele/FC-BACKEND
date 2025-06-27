package br.com.gooddreams.api.dtos;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponseDTO(Long id,
                                 String name,
                                 String description,
                                 BigDecimal price,
                                 String category,
                                 String imageUrl,
                                 Instant createdAt,
                                 Instant updatedAt) {
}
