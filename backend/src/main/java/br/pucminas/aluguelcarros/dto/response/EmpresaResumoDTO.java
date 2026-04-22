package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record EmpresaResumoDTO(
        Long id,
        String razaoSocial,
        String cnpj,
        String ramoDeAtividade
) {
}
