package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ClienteResumoDTO(
        Long id,
        String nome,
        String cpf,
        String rg,
        String endereco,
        String profissao
) {
}
