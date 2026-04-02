package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
}
