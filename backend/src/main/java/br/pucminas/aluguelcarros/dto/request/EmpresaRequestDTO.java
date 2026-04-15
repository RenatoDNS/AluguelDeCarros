package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record EmpresaRequestDTO(
        @NotBlank String razaoSocial,
        @NotBlank String cnpj,
        @NotBlank String ramoDeAtividade,
        @NotBlank String senha
) {
}


