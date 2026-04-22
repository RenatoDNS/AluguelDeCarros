package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Serdeable
public record ContratoRequestDTO(
        @NotNull Long empresaId,
        @NotNull Long clienteId,
        @NotNull Long veiculoId,
        @NotNull LocalDate dataInicioAluguel,
        @NotNull LocalDate dataFimAluguel,
        @NotNull @Positive BigDecimal valorTotal,
        @NotNull @Positive BigDecimal valorDiaria
) {
}

