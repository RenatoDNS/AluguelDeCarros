package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AutomovelResponseDTO(
        Long id,
        String matricula,
        String placa,
        Integer ano,
        String marca,
        String modelo,
        AutomovelStatus status
) {
}


