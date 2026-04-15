package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.ContratoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoResponseDTO;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.service.ContratoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ContratoFacade {

    private final ContratoService contratoService;

    @Inject
    public ContratoFacade(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    public ContratoResponseDTO cadastrar(ContratoRequestDTO dto) {
        return toResponse(contratoService.cadastrar(fromDto(dto)));
    }

    public ContratoResponseDTO buscar(Long id) {
        return toResponse(contratoService.buscarPorId(id));
    }

    public List<ContratoResponseDTO> listar() {
        return contratoService.listar().stream().map(ContratoFacade::toResponse).toList();
    }

    public ContratoResponseDTO atualizar(Long id, ContratoRequestDTO dto) {
        Contrato contrato = fromDto(dto);
        contrato.setId(id);
        return toResponse(contratoService.atualizar(contrato));
    }

    public void remover(Long id) {
        contratoService.deletar(id);
    }

    public ContratoResponseDTO executar(Long id) {
        return toResponse(contratoService.executar(id));
    }

    public ContratoResponseDTO executarPorPedido(Long pedidoId) {
        return toResponse(contratoService.executarPorPedido(pedidoId));
    }

    private Contrato fromDto(ContratoRequestDTO dto) {
        Contrato contrato = new Contrato();

        Pedido pedido = new Pedido();
        pedido.setId(dto.pedidoId());

        contrato.setPedido(pedido);
        contrato.setTipoPropriedade(contratoService.validarTipoPropriedade(dto.tipoPropriedade()));
        contrato.setDataAssinatura(dto.dataAssinatura());

        return contrato;
    }

    private static ContratoResponseDTO toResponse(Contrato contrato) {
        return new ContratoResponseDTO(
                contrato.getId(),
                contrato.getPedido().getId(),
                contrato.getPedido().getAutomovel().getId(),
                contrato.getTipoPropriedade(),
                contrato.getDataAssinatura()
        );
    }
}

