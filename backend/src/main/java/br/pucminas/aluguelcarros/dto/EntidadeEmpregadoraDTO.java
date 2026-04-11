package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record EntidadeEmpregadoraDTO(
        @NotBlank String nomeEmpresa,
        @NotBlank String cnpj,
        @NotNull Double rendimento
) {
}
