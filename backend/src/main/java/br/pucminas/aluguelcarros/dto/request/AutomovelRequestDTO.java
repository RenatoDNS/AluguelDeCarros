package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record AutomovelRequestDTO(
        @NotBlank String matricula,
        @NotBlank String placa,
        @NotNull Integer ano,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotBlank String status
) {
}


