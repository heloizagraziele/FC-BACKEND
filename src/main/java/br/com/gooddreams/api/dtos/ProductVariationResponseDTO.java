package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Product;

public record ProductVariationResponseDTO(Long id,
                                          String size,
                                          Integer stock,
                                          Product product) {
}
