package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.ContratoCreditoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoCreditoResponseDTO;
import br.pucminas.aluguelcarros.facade.ContratoCreditoFacade;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/contratos-credito")
public class ContratoCreditoController {

    //TODO assinar contrato somente

    private final ContratoCreditoFacade contratoCreditoFacade;
    private final AuthService authService;

    @Inject
    public ContratoCreditoController(ContratoCreditoFacade contratoCreditoFacade, AuthService authService) {
        this.contratoCreditoFacade = contratoCreditoFacade;
        this.authService = authService;
    }

    @Post
    public ContratoCreditoResponseDTO cadastrar(@Body @Valid ContratoCreditoRequestDTO dto, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return contratoCreditoFacade.cadastrar(dto, perfil.id(), perfil.userType());
    }

    @Get
    public List<ContratoCreditoResponseDTO> listar() {
        return contratoCreditoFacade.listar();
    }

    @Get("/{id}")
    public ContratoCreditoResponseDTO buscar(@PathVariable Long id) {
        return contratoCreditoFacade.buscar(id);
    }

    @Put("/{id}")
    public ContratoCreditoResponseDTO atualizar(@PathVariable Long id,
                                                @Body @Valid ContratoCreditoRequestDTO dto,
                                                HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return contratoCreditoFacade.atualizar(id, dto, perfil.id(), perfil.userType());
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        contratoCreditoFacade.remover(id, perfil.id(), perfil.userType());
        return HttpResponse.noContent();
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }
}


