package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Serdeable
public class LoginRequestDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String senha;
}
