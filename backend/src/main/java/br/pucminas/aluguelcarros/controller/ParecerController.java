package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.ParecerRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ParecerResponseDTO;
import br.pucminas.aluguelcarros.facade.ParecerFacade;
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

@Controller("/pareceres")
public class ParecerController {

    private final ParecerFacade parecerFacade;

    @Inject
    public ParecerController(ParecerFacade parecerFacade) {
        this.parecerFacade = parecerFacade;
    }

    @Post
    public ParecerResponseDTO cadastrar(@Body @Valid ParecerRequestDTO dto) {
        return parecerFacade.cadastrar(dto);
    }

    @Get
    public List<ParecerResponseDTO> listar() {
        return parecerFacade.listar();
    }

    @Get("/{id}")
    public ParecerResponseDTO buscarPorId(@PathVariable Long id) {
        return parecerFacade.buscar(id);
    }

    @Put("/{id}")
    public ParecerResponseDTO atualizar(@PathVariable Long id, @Body @Valid ParecerRequestDTO dto) {
        return parecerFacade.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        parecerFacade.remover(id);
        return HttpResponse.noContent();
    }
}

