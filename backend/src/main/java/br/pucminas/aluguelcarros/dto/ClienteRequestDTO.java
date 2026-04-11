package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Serdeable
public record ClienteRequestDTO(
        @NotBlank String rg,
        @NotBlank String cpf,
        @NotBlank String nome,
        @NotBlank String endereco,
        @NotBlank String profissao,
        @NotBlank String senha,
        @NotEmpty @Size(min = 1, max = 3) @Valid List<EntidadeEmpregadoraDTO> entidadesEmpregadoras
) {
}
