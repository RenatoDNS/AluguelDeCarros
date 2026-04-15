package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Serdeable
public record PedidoRequestDTO(
        @NotNull Long clienteId,
        @NotNull Long automovelId,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim,
        @Nullable String status
) {
}

