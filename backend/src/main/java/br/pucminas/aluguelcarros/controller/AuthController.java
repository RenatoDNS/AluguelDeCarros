package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.config.JwtConfig;
import br.pucminas.aluguelcarros.dto.LoginRequestDTO;
import br.pucminas.aluguelcarros.dto.TokenResponseDTO;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.Optional;

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
            String token = authService.autenticar(dto.getLogin(), dto.getSenha());
            return HttpResponse.ok(new TokenResponseDTO(token, jwtConfig.getExpirationMs()));
        } catch (IllegalArgumentException e) {
            return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Credenciais inválidas."));
        }
    }

    @Post("/logout")
    public HttpResponse<Void> logout(@Header("Authorization") Optional<String> authorization) {
        String token = authorization
                .filter(a -> a.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length()))
                .map(a -> a.substring("Bearer ".length()).trim())
                .orElse("");
        if (token.isEmpty() || !authService.validarToken(token)) {
            return HttpResponse.status(HttpStatus.UNAUTHORIZED);
        }
        return HttpResponse.noContent();
    }
}
