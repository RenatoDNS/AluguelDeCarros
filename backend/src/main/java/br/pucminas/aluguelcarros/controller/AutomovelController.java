package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.AutomovelRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.AutomovelResponseDTO;
import br.pucminas.aluguelcarros.facade.AutomovelFacade;
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

@Controller("/automoveis")
public class AutomovelController {

    private final AutomovelFacade automovelFacade;
    private final AuthService authService;

    @Inject
    public AutomovelController(AutomovelFacade automovelFacade,
                               AuthService authService) {
        this.automovelFacade = automovelFacade;
        this.authService = authService;
    }

    @Post
    public AutomovelResponseDTO cadastrar(@Body @Valid AutomovelRequestDTO dto, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return automovelFacade.cadastrar(dto, perfil.id(), perfil.userType());
    }

    @Get
    public List<AutomovelResponseDTO> listar() {
        return automovelFacade.listar();
    }

    @Get("/me")
    public List<AutomovelResponseDTO> listarMe(HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return automovelFacade.listarMe(perfil.id(), perfil.userType());
    }

    @Get("/{id}")
    public AutomovelResponseDTO buscarPorId(@PathVariable Long id) {
        return automovelFacade.buscar(id);
    }

    @Get("/status/{status}")
    public List<AutomovelResponseDTO> listarPorStatus(@PathVariable String status) {
        return automovelFacade.listarPorStatus(status);
    }

    @Put("/{id}")
    public AutomovelResponseDTO atualizar(@PathVariable Long id, @Body @Valid AutomovelRequestDTO dto) {
        return automovelFacade.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        automovelFacade.remover(id);
        return HttpResponse.noContent();
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }
}



