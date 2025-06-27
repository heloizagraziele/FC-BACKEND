package br.com.gooddreams.api.dtos;

import java.time.Instant;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        Instant createdAt,
        Instant updatedAt
) {
}
