package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.EmpresaRequestDTO;
import br.pucminas.aluguelcarros.dto.response.EmpresaResponseDTO;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.service.EmpresaService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class EmpresaFacade {

    private final EmpresaService empresaService;

    @Inject
    public EmpresaFacade(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    public EmpresaResponseDTO cadastrar(EmpresaRequestDTO dto) {
        return toResponse(empresaService.cadastrar(fromDto(dto)));
    }

    private static Empresa fromDto(EmpresaRequestDTO dto) {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(dto.razaoSocial());
        empresa.setCnpj(dto.cnpj());
        empresa.setRamoDeAtividade(dto.ramoDeAtividade());
        empresa.setSenha(dto.senha());
        return empresa;
    }

    private static EmpresaResponseDTO toResponse(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getRazaoSocial(),
                empresa.getCnpj(),
                empresa.getRamoDeAtividade()
        );
    }
}

