package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.TipoPropriedade;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record ContratoResponseDTO(
        Long id,
        Long pedidoId,
        Long automovelId,
        TipoPropriedade tipoPropriedade,
        LocalDate dataAssinatura
) {
}

