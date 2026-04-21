package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.BancoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.BancoResponseDTO;
import br.pucminas.aluguelcarros.facade.BancoFacade;
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

@Controller("/bancos")
public class BancoController {

    private final BancoFacade bancoFacade;

    @Inject
    public BancoController(BancoFacade bancoFacade) {
        this.bancoFacade = bancoFacade;
    }

    @Post
    public BancoResponseDTO cadastrar(@Body @Valid BancoRequestDTO dto) {
        return bancoFacade.cadastrar(dto);
    }
}

