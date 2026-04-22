package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoResponseDTO;
import br.pucminas.aluguelcarros.facade.ContratoFacade;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/contratos")
public class ContratoController {

    private final ContratoFacade contratoFacade;
    private final AuthService authService;

    @Inject
    public ContratoController(ContratoFacade contratoFacade,
                              AuthService authService) {
        this.contratoFacade = contratoFacade;
        this.authService = authService;
    }

    @Get("/{id}")
    public ContratoResponseDTO buscar(@PathVariable Long id) {
        return contratoFacade.buscar(id);
    }

    @Post("/{id}/assinar")
    public ContratoResponseDTO assinar(@PathVariable Long id, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return contratoFacade.assinar(id, perfil.id(), perfil.userType());
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }
}
