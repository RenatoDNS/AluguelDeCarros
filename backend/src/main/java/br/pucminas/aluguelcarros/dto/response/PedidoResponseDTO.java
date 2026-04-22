package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record PedidoResponseDTO(
        Long id,
        String numeroProtocolo,
        Long clienteId,
        Long automovelId,
        PedidoTipo tipoPedido,
        LocalDate dataInicio,
        LocalDate dataFim,
        Integer qntdParcelas,
        PedidoStatus status,
        String justificativa
) {
}

