package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Serdeable
public record PedidoAluguelRequestDTO(
        @NotNull Long clienteId,
        @NotNull Long automovelId,
        @NotNull LocalDate dataInicio,
        @NotNull LocalDate dataFim
) {
}

