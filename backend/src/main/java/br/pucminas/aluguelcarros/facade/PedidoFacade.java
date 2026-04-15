package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.PedidoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.PedidoResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.service.PedidoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class PedidoFacade {

    private final PedidoService pedidoService;

    @Inject
    public PedidoFacade(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    public PedidoResponseDTO cadastrar(PedidoRequestDTO dto) {
        return toResponse(pedidoService.cadastrar(fromDto(dto)));
    }

    public PedidoResponseDTO buscar(Long id) {
        return toResponse(pedidoService.buscarPorId(id));
    }

    public List<PedidoResponseDTO> listar() {
        return pedidoService.listar().stream().map(PedidoFacade::toResponse).toList();
    }

    public List<PedidoResponseDTO> listarPorStatus(String status) {
        return pedidoService.listarPorStatus(status).stream().map(PedidoFacade::toResponse).toList();
    }

    public PedidoResponseDTO atualizar(Long id, PedidoRequestDTO dto) {
        Pedido pedido = fromDto(dto);
        pedido.setId(id);
        return toResponse(pedidoService.atualizar(pedido));
    }

    public void remover(Long id) {
        pedidoService.deletar(id);
    }

    public PedidoResponseDTO cancelar(Long id, Long clienteAutenticadoId, UserType userType) {
        return toResponse(pedidoService.cancelar(id, clienteAutenticadoId, userType));
    }

    public List<PedidoResponseDTO> listarMe(Long clienteAutenticadoId, UserType userType) {
        return pedidoService.listarPedidosDoCliente(clienteAutenticadoId, userType)
                .stream()
                .map(PedidoFacade::toResponse)
                .toList();
    }

    public List<PedidoResponseDTO> listarEmAnaliseParaAgente(Long agenteId, UserType userType) {
        return pedidoService.listarEmAnaliseParaAgente(agenteId, userType)
                .stream()
                .map(PedidoFacade::toResponse)
                .toList();
    }

    private Pedido fromDto(PedidoRequestDTO dto) {
        Pedido pedido = new Pedido();

        Cliente cliente = new Cliente();
        cliente.setId(dto.clienteId());

        Automovel automovel = new Automovel();
        automovel.setId(dto.automovelId());

        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setDataInicio(dto.dataInicio());
        pedido.setDataFim(dto.dataFim());

        if (dto.status() != null && !dto.status().isBlank()) {
            pedido.setStatus(pedidoService.validarStatus(dto.status()));
        }

        return pedido;
    }

    public static PedidoResponseDTO toResponse(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getNumeroProtocolo(),
                pedido.getCliente().getId(),
                pedido.getAutomovel().getId(),
                pedido.getDataInicio(),
                pedido.getDataFim(),
                pedido.getStatus()
        );
    }
}

