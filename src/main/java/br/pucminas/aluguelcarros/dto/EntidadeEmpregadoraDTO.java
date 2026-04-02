package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Serdeable
public class EntidadeEmpregadoraDTO {

    @NotBlank
    private String nomeEmpresa;

    @NotBlank
    private String cnpj;

    @NotNull
    private Double rendimento;
}
