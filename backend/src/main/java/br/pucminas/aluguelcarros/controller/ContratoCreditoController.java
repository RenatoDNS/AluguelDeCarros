package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoCreditoResponseDTO;
import br.pucminas.aluguelcarros.facade.ContratoCreditoFacade;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;

@Controller("/contratos-credito")
public class ContratoCreditoController {

    private final ContratoCreditoFacade contratoCreditoFacade;
    private final AuthService authService;

    @Inject
    public ContratoCreditoController(ContratoCreditoFacade contratoCreditoFacade,
                                     AuthService authService) {
        this.contratoCreditoFacade = contratoCreditoFacade;
        this.authService = authService;
    }

    @Get("/{id}")
    public ContratoCreditoResponseDTO buscar(@PathVariable Long id) {
        return contratoCreditoFacade.buscar(id);
    }

    @Post("/{id}/assinar")
    public ContratoCreditoResponseDTO assinar(@PathVariable Long id, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return contratoCreditoFacade.assinar(id, perfil.id(), perfil.userType());
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }
}
