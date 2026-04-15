package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Serdeable
public record ContratoRequestDTO(
        @NotNull Long pedidoId,
        @NotBlank String tipoPropriedade,
        @NotNull LocalDate dataAssinatura
) {
}

