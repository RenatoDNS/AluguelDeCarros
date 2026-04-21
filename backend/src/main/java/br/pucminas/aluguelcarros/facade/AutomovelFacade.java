package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.AutomovelRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AutomovelResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.service.AutomovelService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class AutomovelFacade {

    private final AutomovelService automovelService;

    @Inject
    public AutomovelFacade(AutomovelService automovelService) {
        this.automovelService = automovelService;
    }

    public AutomovelResponseDTO cadastrar(AutomovelRequestDTO dto, Long agentId, UserType userType) {
        return toResponse(automovelService.cadastrar(fromDto(dto), agentId, userType));
    }

    public AutomovelResponseDTO buscar(Long id) {
        return toResponse(automovelService.buscarPorId(id));
    }

    public List<AutomovelResponseDTO> listar() {
        return automovelService.listar().stream()
                .map(AutomovelFacade::toResponse)
                .toList();
    }

    public List<AutomovelResponseDTO> listarMe(Long agentId, UserType userType) {
        return automovelService.listarMe(agentId, userType).stream()
                .map(AutomovelFacade::toResponse)
                .toList();
    }

    public List<AutomovelResponseDTO> listarPorStatus(String status) {
        return automovelService.listarPorStatus(status).stream()
                .map(AutomovelFacade::toResponse)
                .toList();
    }

    public AutomovelResponseDTO atualizar(Long id, AutomovelRequestDTO dto) {
        Automovel automovel = fromDto(dto);
        automovel.setId(id);
        return toResponse(automovelService.atualizar(automovel));
    }

    public void remover(Long id, Long agentId, UserType userType) {
        automovelService.deletar(id, agentId, userType);
    }

    private Automovel fromDto(AutomovelRequestDTO dto) {
        Automovel automovel = new Automovel();
        automovel.setMatricula(dto.matricula());
        automovel.setPlaca(dto.placa());
        automovel.setAno(dto.ano());
        automovel.setMarca(dto.marca());
        automovel.setModelo(dto.modelo());
        automovel.setDiaria(dto.diaria());
        automovel.setStatus(automovelService.validarStatus(dto.status()));
        return automovel;
    }

    private static AutomovelResponseDTO toResponse(Automovel automovel) {
        return new AutomovelResponseDTO(
                automovel.getId(),
                automovel.getMatricula(),
                automovel.getPlaca(),
                automovel.getAno(),
                automovel.getMarca(),
                automovel.getModelo(),
                automovel.getDiaria(),
                automovel.getAgentId(),
                automovel.getAgentType(),
                automovel.getStatus()
        );
    }
}

