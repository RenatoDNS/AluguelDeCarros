package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.ParecerRequestDTO;
import br.pucminas.aluguelcarros.dto.request.PedidoAvaliacaoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ParecerResponseDTO;
import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.model.Parecer;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.service.ParecerService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ParecerFacade {

    private final ParecerService parecerService;

    @Inject
    public ParecerFacade(ParecerService parecerService) {
        this.parecerService = parecerService;
    }

    public ParecerResponseDTO cadastrar(ParecerRequestDTO dto) {
        AgenteTipo agenteTipo = parecerService.validarAgenteTipo(dto.agenteTipo());
        Parecer parecer = parecerService.cadastrar(fromDto(dto), agenteTipo, dto.agenteId());
        return toResponse(parecer);
    }

    public ParecerResponseDTO buscar(Long id) {
        return toResponse(parecerService.buscarPorId(id));
    }

    public List<ParecerResponseDTO> listar() {
        return parecerService.listar().stream().map(ParecerFacade::toResponse).toList();
    }

    public ParecerResponseDTO atualizar(Long id, ParecerRequestDTO dto) {
        AgenteTipo agenteTipo = parecerService.validarAgenteTipo(dto.agenteTipo());
        Parecer parecer = parecerService.atualizar(id, fromDto(dto), agenteTipo, dto.agenteId());
        return toResponse(parecer);
    }

    public void remover(Long id) {
        parecerService.deletar(id);
    }

    public ParecerResponseDTO avaliarPedido(Long pedidoId, PedidoAvaliacaoRequestDTO dto, AgenteTipo agenteTipo, Long agenteId) {
        Parecer parecer = parecerService.avaliarPedido(
                pedidoId,
                agenteTipo,
                agenteId,
                parecerService.validarResultado(dto.resultado()),
                dto.justificativa()
        );
        return toResponse(parecer);
    }

    private Parecer fromDto(ParecerRequestDTO dto) {
        Parecer parecer = new Parecer();
        Pedido pedido = new Pedido();
        pedido.setId(dto.pedidoId());

        parecer.setPedido(pedido);
        parecer.setResultado(parecerService.validarResultado(dto.resultado()));
        parecer.setJustificativa(dto.justificativa().trim());
        parecer.setDataEmissao(dto.dataEmissao());

        return parecer;
    }

    private static ParecerResponseDTO toResponse(Parecer parecer) {
        AgenteTipo agenteTipo;
        Long agenteId;

        if (parecer.getBanco() != null) {
            agenteTipo = AgenteTipo.BANCO;
            agenteId = parecer.getBanco().getId();
        } else {
            agenteTipo = AgenteTipo.EMPRESA;
            agenteId = parecer.getEmpresa().getId();
        }

        return new ParecerResponseDTO(
                parecer.getId(),
                parecer.getPedido().getId(),
                agenteTipo,
                agenteId,
                parecer.getResultado(),
                parecer.getJustificativa(),
                parecer.getDataEmissao()
        );
    }
}

