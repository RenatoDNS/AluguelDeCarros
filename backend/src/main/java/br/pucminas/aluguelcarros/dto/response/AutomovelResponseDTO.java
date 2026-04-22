package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record AutomovelResponseDTO(
        Long id,
        String matricula,
        String placa,
        Integer ano,
        String marca,
        String modelo,
        BigDecimal valor,
        String linkImagem,
        BigDecimal taxaJuros,
        Long agentId,
        AgenteTipo agentType,
        AutomovelStatus status
) {
}


