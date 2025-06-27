package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Product;

public record ProductVariationCreateDTO(
        String size,
        Integer stock,
        Product product) {}

