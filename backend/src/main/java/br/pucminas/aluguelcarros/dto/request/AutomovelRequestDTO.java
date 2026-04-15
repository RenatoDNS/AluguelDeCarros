package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Serdeable
public record AutomovelRequestDTO(
        @NotBlank String matricula,
        @NotBlank String placa,
        @NotNull Integer ano,
        @NotBlank String marca,
        @NotBlank String modelo,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) @Digits(integer = 8, fraction = 2) BigDecimal diaria,
        @NotBlank String status
) {
}


