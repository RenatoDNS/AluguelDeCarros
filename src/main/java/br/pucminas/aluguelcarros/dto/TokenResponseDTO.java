package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Serdeable
public class TokenResponseDTO {

    private String token;
    private Long expiresIn;
}
