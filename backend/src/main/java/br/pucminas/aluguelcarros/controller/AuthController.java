package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.config.JwtConfig;
import br.pucminas.aluguelcarros.dto.LoginRequestDTO;
import br.pucminas.aluguelcarros.dto.TokenResponseDTO;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.Map;

@Controller("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtConfig jwtConfig;

    @Inject
    public AuthController(AuthService authService, JwtConfig jwtConfig) {
        this.authService = authService;
        this.jwtConfig = jwtConfig;
    }

    @Post("/login")
    public HttpResponse<?> login(@Body @Valid LoginRequestDTO dto) {
        try {
            String token = authService.autenticar(dto.login(), dto.senha());
            return HttpResponse.ok(new TokenResponseDTO(token, jwtConfig.getExpirationMs()));
        } catch (IllegalArgumentException e) {
            return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Credenciais inválidas."));
        }
    }
}
