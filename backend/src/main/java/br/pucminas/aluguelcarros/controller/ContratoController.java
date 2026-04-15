package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.ContratoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoResponseDTO;
import br.pucminas.aluguelcarros.facade.ContratoFacade;
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

@Controller("/contratos")
public class ContratoController {

    private final ContratoFacade contratoFacade;

    @Inject
    public ContratoController(ContratoFacade contratoFacade) {
        this.contratoFacade = contratoFacade;
    }

    @Post
    public ContratoResponseDTO cadastrar(@Body @Valid ContratoRequestDTO dto) {
        return contratoFacade.cadastrar(dto);
    }

    @Get
    public List<ContratoResponseDTO> listar() {
        return contratoFacade.listar();
    }

    @Get("/{id}")
    public ContratoResponseDTO buscar(@PathVariable Long id) {
        return contratoFacade.buscar(id);
    }

    @Put("/{id}")
    public ContratoResponseDTO atualizar(@PathVariable Long id, @Body @Valid ContratoRequestDTO dto) {
        return contratoFacade.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        contratoFacade.remover(id);
        return HttpResponse.noContent();
    }

    @Post("/{id}/executar")
    public ContratoResponseDTO executar(@PathVariable Long id) {
        return contratoFacade.executar(id);
    }
}

