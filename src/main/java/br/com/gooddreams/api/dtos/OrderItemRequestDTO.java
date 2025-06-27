package br.com.gooddreams.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO(
        Long productId,
        Integer quantity
) {}
