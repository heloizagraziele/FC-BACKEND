package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Customer;

public record CartResponseDTO(Long id,
                              Customer customer) {
}
