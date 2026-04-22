package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.EmpresaRequestDTO;
import br.pucminas.aluguelcarros.dto.response.EmpresaResponseDTO;
import br.pucminas.aluguelcarros.facade.EmpresaFacade;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

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

