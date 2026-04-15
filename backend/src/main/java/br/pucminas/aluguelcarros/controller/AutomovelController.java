package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.AutomovelRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AutomovelResponseDTO;
import br.pucminas.aluguelcarros.facade.AutomovelFacade;
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

    @Inject
    public AutomovelController(AutomovelFacade automovelFacade) {
        this.automovelFacade = automovelFacade;
    }

    @Post
    public AutomovelResponseDTO cadastrar(@Body @Valid AutomovelRequestDTO dto) {
        return automovelFacade.cadastrar(dto);
    }

    @Get
    public List<AutomovelResponseDTO> listar() {
        return automovelFacade.listar();
    }

    @Get("/{filtro}")
    public HttpResponse<?> buscarPorIdOuStatus(@PathVariable String filtro) {
        if (filtro != null && filtro.matches("\\d+")) {
            return HttpResponse.ok(automovelFacade.buscar(Long.parseLong(filtro)));
        }
        return HttpResponse.ok(automovelFacade.listarPorStatus(filtro));
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
}



