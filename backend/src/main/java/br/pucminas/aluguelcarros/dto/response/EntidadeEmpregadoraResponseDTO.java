package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record EntidadeEmpregadoraResponseDTO(
        String nomeEmpresa,
        String cnpj,
        Double rendimento
) {
}

