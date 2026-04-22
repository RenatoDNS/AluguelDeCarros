package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable
public record VeiculoResumoDTO(
        Long id,
        String matricula,
        String placa,
        Integer ano,
        String marca,
        String modelo,
        BigDecimal valor,
        String linkImagem
) {
}
