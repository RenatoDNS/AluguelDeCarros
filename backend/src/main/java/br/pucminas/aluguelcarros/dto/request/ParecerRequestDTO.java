package br.pucminas.aluguelcarros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.micronaut.serde.annotation.Serdeable;

import java.time.LocalDate;

@Serdeable
public record ParecerRequestDTO(
        @NotNull Long pedidoId,
        @NotBlank String agenteTipo,
        @NotNull Long agenteId,
        @NotBlank String resultado,
        @NotBlank String justificativa,
        @NotNull LocalDate dataEmissao
) {
}

