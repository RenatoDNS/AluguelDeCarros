package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpf
) {
}
