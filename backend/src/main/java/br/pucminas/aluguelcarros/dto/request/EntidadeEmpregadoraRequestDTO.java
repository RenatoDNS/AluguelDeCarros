package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record EntidadeEmpregadoraRequestDTO(
        @NotBlank String nomeEmpresa,
        @NotBlank String cnpj,
        @NotNull Double rendimento
) {
}

