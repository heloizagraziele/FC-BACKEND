package br.com.gooddreams.api.dtos;

public record AddressResponseDTO(
        Long id,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        Long zipcode,
        Long customerId
) {}