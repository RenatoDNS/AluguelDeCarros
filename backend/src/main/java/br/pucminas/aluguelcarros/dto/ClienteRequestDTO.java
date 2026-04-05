package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Serdeable
public class ClienteRequestDTO {

    @NotBlank
    private String rg;

    @NotBlank
    private String cpf;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @NotBlank
    private String profissao;

    @NotBlank
    private String senha;

    @NotEmpty
    @Size(min = 1, max = 3)
    @Valid
    private List<EntidadeEmpregadoraDTO> entidadesEmpregadoras;
}
