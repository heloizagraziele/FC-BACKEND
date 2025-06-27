package br.com.gooddreams.api.dtos;

public record CustomerUpdateDTO(
        Long id,
        String name,
        String email,
        String password
) {
}
