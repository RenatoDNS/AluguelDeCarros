package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.config.JwtConfig;
import br.pucminas.aluguelcarros.dto.request.LoginRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.TokenResponseDTO;
import br.pucminas.aluguelcarros.service.AuthService;
import br.pucminas.aluguelcarros.service.AuthService.AuthResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
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
            AuthResult authResult = authService.autenticar(dto.login(), dto.senha());
            return HttpResponse.ok(new TokenResponseDTO(
                    authResult.token(),
                    jwtConfig.getExpirationMs(),
                    authResult.userType()
            ));
        } catch (IllegalArgumentException e) {
            return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Credenciais inválidas."));
        }
    }

    @Get("/me")
    public HttpResponse<?> me(HttpRequest<?> request) {
        try {
            String authorization = request.getHeaders().getAuthorization().orElse("");
            AuthMeResponseDTO response = authService.obterPerfilAutenticado(authorization);
            return HttpResponse.ok(response);
        } catch (RuntimeException e) {
            return HttpResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensagem", "Token inválido."));
        }
    }
}
