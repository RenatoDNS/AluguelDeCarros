package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.ClienteRequestDTO;
import br.pucminas.aluguelcarros.dto.ClienteResponseDTO;
import br.pucminas.aluguelcarros.facade.ClienteFacade;
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

@Controller("/clientes")
public class ClienteController {

    private final ClienteFacade clienteFacade;

    @Inject
    public ClienteController(ClienteFacade clienteFacade) {
        this.clienteFacade = clienteFacade;
    }

    @Post
    public ClienteResponseDTO cadastrar(@Body @Valid ClienteRequestDTO dto) {
        return clienteFacade.cadastrar(dto);
    }

    @Get("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Long id) {
        return clienteFacade.buscar(id);
    }

    @Put("/{id}")
    public ClienteResponseDTO atualizar(@PathVariable Long id, @Body @Valid ClienteRequestDTO dto) {
        return clienteFacade.atualizar(id, dto);
    }

    @Delete("/{id}")
    public HttpResponse<Void> deletar(@PathVariable Long id) {
        clienteFacade.remover(id);
        return HttpResponse.noContent();
    }
}
