package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record PedidoAvaliacaoRequestDTO(
        @NotBlank String resultado,
        @NotBlank String justificativa
) {
}

