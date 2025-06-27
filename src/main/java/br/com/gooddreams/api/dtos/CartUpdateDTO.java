package br.com.gooddreams.api.dtos;

import br.com.gooddreams.api.entities.Customer;

public record CartUpdateDTO(
        Long id,
        Customer customer
) {}
