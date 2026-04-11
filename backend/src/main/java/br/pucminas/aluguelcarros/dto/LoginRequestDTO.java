package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record LoginRequestDTO(
        @NotBlank String login,
        @NotBlank String senha
) {
}
