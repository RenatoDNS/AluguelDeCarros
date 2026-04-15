package br.pucminas.aluguelcarros.dto.response;

import br.pucminas.aluguelcarros.enums.UserType;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record AuthMeResponseDTO(
        Long id,
        String login,
        UserType userType
) {
}


