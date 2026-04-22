package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Serdeable
public record ContratoCreditoRequestDTO(
        @NotNull Long bancoId,
        @NotNull Long clienteId,
        @NotNull Long veiculoId,
        @NotNull @Positive Integer quantidadeParcelas,
        @NotNull @Positive BigDecimal valorParcela,
        @NotNull @Positive BigDecimal valorTotal
) {
}

