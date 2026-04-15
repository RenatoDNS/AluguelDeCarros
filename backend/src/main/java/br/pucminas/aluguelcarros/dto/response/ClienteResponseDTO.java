package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record ClienteResponseDTO(
        Long id,
        String rg,
        String nome,
        String cpf,
        String endereco,
        String profissao,
        List<EntidadeEmpregadoraResponseDTO> entidadesEmpregadoras
) {
}


