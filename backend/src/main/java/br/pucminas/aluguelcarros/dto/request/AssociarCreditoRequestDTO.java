package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Serdeable
public record AssociarCreditoRequestDTO(
        @NotNull @Positive Double valorFinanciado,
        @NotNull @Positive Double taxaJuros,
        @NotNull @Positive Integer prazoMeses,
        @Nullable LocalDate dataAssinatura
) {
}

