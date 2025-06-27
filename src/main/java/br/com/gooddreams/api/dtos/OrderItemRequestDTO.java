package br.com.gooddreams.api.dtos;


public record OrderItemRequestDTO(
        Long productId,
        Integer quantity
) {}
