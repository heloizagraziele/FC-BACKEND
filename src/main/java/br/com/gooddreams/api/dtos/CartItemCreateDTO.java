package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Cart;
import br.com.gooddreams.api.entities.ProductVariation;

public record CartItemCreateDTO(
        Cart cart,
        ProductVariation productVariation,
        Integer quantity,
        Double unitPrice) {}
