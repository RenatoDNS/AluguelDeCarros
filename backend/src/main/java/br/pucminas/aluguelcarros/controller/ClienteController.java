package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.ClienteRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ClienteResponseDTO;
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

import java.util.List;

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
}
