package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record EmpresaResponseDTO(
        Long id,
        String razaoSocial,
        String cnpj,
        String ramoDeAtividade
) {
}


