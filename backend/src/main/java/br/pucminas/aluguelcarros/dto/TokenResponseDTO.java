package br.pucminas.aluguelcarros.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TokenResponseDTO {

    private String token;
    private Long expiresIn;

    public TokenResponseDTO() {
    }

    public TokenResponseDTO(String token, Long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}
