package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.UserType;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TokenResponseDTO(
        String token,
        Long expiresIn,
        UserType userType
) {
}

