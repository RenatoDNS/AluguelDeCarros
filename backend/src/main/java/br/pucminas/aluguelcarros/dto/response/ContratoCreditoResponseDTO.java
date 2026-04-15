package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ContratoCreditoResponseDTO(
        Long id,
        Long contratoId,
        Long bancoId,
        Double valorFinanciado,
        Double taxaJuros,
        Integer prazoMeses
) {
}

