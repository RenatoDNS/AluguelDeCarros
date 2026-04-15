package br.pucminas.aluguelcarros.dto.request;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record BancoRequestDTO(
        @NotBlank String razaoSocial,
        @NotBlank String cnpj,
        @NotBlank String codigoBancario,
        @NotBlank String senha
) {
}


