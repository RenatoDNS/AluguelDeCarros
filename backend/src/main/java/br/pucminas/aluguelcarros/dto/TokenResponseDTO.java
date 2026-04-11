package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TokenResponseDTO(
        String token,
        Long expiresIn
) {
}
