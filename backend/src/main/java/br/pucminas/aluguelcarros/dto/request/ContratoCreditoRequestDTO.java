package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Serdeable
public record ContratoCreditoRequestDTO(
        @NotNull Long contratoId,
        @NotNull @Positive Double valorFinanciado,
        @NotNull @Positive Double taxaJuros,
        @NotNull @Positive Integer prazoMeses
) {
}

