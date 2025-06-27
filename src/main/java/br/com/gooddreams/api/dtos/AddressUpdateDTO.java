package br.com.gooddreams.api.dtos;

public record AddressUpdateDTO(
        Long id,
        String street,
        String number,
        String neighborhood,
        String city,
        String state,
        String complement,
        String zipcode,
        Long customerId
) {
}
