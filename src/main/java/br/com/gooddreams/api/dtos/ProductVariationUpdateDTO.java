package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Product;

public record ProductVariationUpdateDTO(Long id,
                                        String size,
                                        Integer stock,
                                        Product product) {
}
