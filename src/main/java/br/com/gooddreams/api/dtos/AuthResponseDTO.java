package br.com.gooddreams.api.dtos;
public record AuthResponseDTO(
        String token,
        String name,
        String email,
        Long customerId) {}
