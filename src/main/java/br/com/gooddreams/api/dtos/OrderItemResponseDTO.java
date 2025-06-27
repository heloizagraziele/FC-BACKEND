package br.com.gooddreams.api.dtos;

public record OrderItemResponseDTO(
        Long id,
        Long orderId,
        String productVariationId,
        java.math.BigDecimal unitPrice,
        Integer quantity
        ) {
}
