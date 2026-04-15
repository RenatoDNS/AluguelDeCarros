package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.ParecerResultado;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record ParecerResponseDTO(
        Long id,
        Long pedidoId,
        AgenteTipo agenteTipo,
        Long agenteId,
        ParecerResultado resultado,
        String justificativa,
        LocalDate dataEmissao
) {
}

