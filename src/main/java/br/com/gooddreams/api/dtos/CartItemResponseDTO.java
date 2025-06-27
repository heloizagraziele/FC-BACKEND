package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Cart;
import br.com.gooddreams.api.entities.ProductVariation;

public record CartItemResponseDTO(
        Long id,
        Cart cart,
        ProductVariation productVariation,
        Integer quantity,
        java.math.BigDecimal unitPrice
) {}
