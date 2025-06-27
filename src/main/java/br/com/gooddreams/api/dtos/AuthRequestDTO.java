package br.com.gooddreams.api.dtos;

public record AuthRequestDTO(
        String email,
        String password) {
}
