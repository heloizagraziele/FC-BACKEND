package br.com.gooddreams.api.dtos;

public record AddressResponseDTO(
        Long id,
        String street,
        String number,
        String complement, // Lembre-se: este campo foi colocado antes de neighborhood no DTO
        String neighborhood,
        String city,
        String state,
        Long zipcode, // Mantido como Long no DTO, com conversão String<->Long no mapper
        Long customerId // <--- ID do cliente associado ao endereço
) {}