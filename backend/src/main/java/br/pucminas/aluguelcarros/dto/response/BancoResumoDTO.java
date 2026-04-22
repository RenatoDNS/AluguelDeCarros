package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record BancoResumoDTO(
        Long id,
        String razaoSocial,
        String cnpj,
        String codigoBancario
) {
}
