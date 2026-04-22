package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.PedidoAluguelRequestDTO;
import br.pucminas.aluguelcarros.dto.request.PedidoAvaliacaoRequestDTO;
import br.pucminas.aluguelcarros.dto.request.PedidoCompraRequestDTO;
import br.pucminas.aluguelcarros.dto.response.PedidoResponseDTO;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
import br.pucminas.aluguelcarros.enums.TipoPropriedade;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.service.ContratoCreditoService;
import br.pucminas.aluguelcarros.service.ContratoService;
import br.pucminas.aluguelcarros.service.PedidoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Singleton
public class PedidoFacade {

    private final PedidoService pedidoService;
    private final ContratoService contratoService;
    private final ContratoCreditoService contratoCreditoService;

    @Inject
    public PedidoFacade(PedidoService pedidoService,
                        ContratoService contratoService,
                        ContratoCreditoService contratoCreditoService) {
        this.pedidoService = pedidoService;
        this.contratoService = contratoService;
        this.contratoCreditoService = contratoCreditoService;
    }

    public PedidoResponseDTO cadastrarAluguel(PedidoAluguelRequestDTO dto) {
        return toResponse(pedidoService.cadastrarAluguel(fromAluguelDto(dto)));
    }

    public PedidoResponseDTO cadastrarCompra(PedidoCompraRequestDTO dto) {
        return toResponse(pedidoService.cadastrarCompra(fromCompraDto(dto)));
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

    public List<PedidoResponseDTO> listarPorStatusParaAgente(String status, Long agenteId, UserType userType) {
        List<Pedido> pedidos = pedidoService.listarPorStatusParaAgente(status, agenteId, userType);

        if (pedidoService.validarStatus(status) == PedidoStatus.APROVADO) {
            gerarContratosAutomaticos(pedidos, agenteId, userType);
        }

        return pedidos.stream()
                .map(PedidoFacade::toResponse)
                .toList();
    }

    public PedidoResponseDTO avaliarPedido(Long pedidoId, PedidoAvaliacaoRequestDTO dto, Long agenteId, UserType userType) {
        return toResponse(pedidoService.avaliarPedido(pedidoId, dto.resultado(), dto.justificativa(), agenteId, userType));
    }

    private void gerarContratosAutomaticos(List<Pedido> pedidos, Long agenteId, UserType userType) {
        for (Pedido pedido : pedidos) {
            if (pedido.getTipoPedido() == PedidoTipo.ALUGUEL) {
                contratoService.obterOuCriarPorPedido(pedido.getId(), TipoPropriedade.CLIENTE, LocalDate.now());
                continue;
            }

            contratoCreditoService.obterOuCriarPorPedido(
                    pedido.getId(),
                    agenteId,
                    userType,
                    pedido.getAutomovel().getValor().doubleValue(),
                    obterTaxaJurosCredito(pedido),
                    pedido.getQntdParcelas(),
                    LocalDate.now()
            );
        }
    }

    private static Double obterTaxaJurosCredito(Pedido pedido) {
        BigDecimal taxa = pedido.getAutomovel().getTaxaJuros();
        if (taxa == null) {
            return 0D;
        }
        return taxa.doubleValue();
    }

    private Pedido fromAluguelDto(PedidoAluguelRequestDTO dto) {
        Pedido pedido = pedidoBase(dto.clienteId(), dto.automovelId());
        pedido.setDataInicio(dto.dataInicio());
        pedido.setDataFim(dto.dataFim());
        return pedido;
    }

    private Pedido fromCompraDto(PedidoCompraRequestDTO dto) {
        Pedido pedido = pedidoBase(dto.clienteId(), dto.automovelId());
        pedido.setQntdParcelas(dto.qntdParcelas());
        return pedido;
    }

    private static Pedido pedidoBase(Long clienteId, Long automovelId) {
        Pedido pedido = new Pedido();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Automovel automovel = new Automovel();
        automovel.setId(automovelId);

        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        return pedido;
    }

    public static PedidoResponseDTO toResponse(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getNumeroProtocolo(),
                pedido.getCliente().getId(),
                pedido.getAutomovel().getId(),
                pedido.getTipoPedido(),
                pedido.getDataInicio(),
                pedido.getDataFim(),
                pedido.getQntdParcelas(),
                pedido.getStatus(),
                pedido.getJustificativa()
        );
    }
}
