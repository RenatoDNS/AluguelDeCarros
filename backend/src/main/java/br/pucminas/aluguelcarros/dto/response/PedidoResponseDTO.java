package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.PedidoStatus;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record PedidoResponseDTO(
        Long id,
        String numeroProtocolo,
        Long clienteId,
        Long automovelId,
        LocalDate dataInicio,
        LocalDate dataFim,
        PedidoStatus status
) {
}

