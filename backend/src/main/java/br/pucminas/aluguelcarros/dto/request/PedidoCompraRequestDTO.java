package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Serdeable
public record PedidoCompraRequestDTO(
        @NotNull Long clienteId,
        @NotNull Long automovelId,
        @NotNull @Positive Integer qntdParcelas
) {
}

