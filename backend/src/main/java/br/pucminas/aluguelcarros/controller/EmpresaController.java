package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.EmpresaRequestDTO;
import br.pucminas.aluguelcarros.dto.response.EmpresaResponseDTO;
import br.pucminas.aluguelcarros.facade.EmpresaFacade;
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

@Controller("/empresas")
public class EmpresaController {

    private final EmpresaFacade empresaFacade;

    @Inject
    public EmpresaController(EmpresaFacade empresaFacade) {
        this.empresaFacade = empresaFacade;
    }

    @Post
    public EmpresaResponseDTO cadastrar(@Body @Valid EmpresaRequestDTO dto) {
        return empresaFacade.cadastrar(dto);
    }
}

