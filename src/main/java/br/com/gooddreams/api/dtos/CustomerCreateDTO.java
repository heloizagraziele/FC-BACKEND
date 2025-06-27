package br.com.gooddreams.api.dtos;

public record CustomerCreateDTO(
        String name,
        String email,
        String password) {
}
