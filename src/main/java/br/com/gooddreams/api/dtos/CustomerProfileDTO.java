package br.com.gooddreams.api.dtos;

import java.util.List;

public record CustomerProfileDTO(
        String name,
        String email,
        List<AddressResponseDTO> addresses
) {}
