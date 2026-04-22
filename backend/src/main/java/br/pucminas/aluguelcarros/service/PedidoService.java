package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

@Singleton
public class PedidoService {

    private static final String STATUS_VALIDOS = "EM_ANALISE, APROVADO, REJEITADO, CANCELADO";

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;

    @Inject
    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         AutomovelRepository automovelRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.automovelRepository = automovelRepository;
    }

    @Transactional
    public Pedido cadastrarAluguel(Pedido pedido) {
        validarPeriodo(pedido);
        pedido.setQntdParcelas(null);
        return cadastrarPedido(pedido, PedidoTipo.ALUGUEL);
    }

    @Transactional
    public Pedido cadastrarCompra(Pedido pedido) {
        validarParcelas(pedido);
        pedido.setDataInicio(null);
        pedido.setDataFim(null);
        return cadastrarPedido(pedido, PedidoTipo.COMPRA);
    }

    @Transactional
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    @Transactional
    public Pedido cancelar(Long pedidoId, Long clienteAutenticadoId, UserType userType) {
        validarPerfilCliente(userType);

        Pedido pedido = buscarPorId(pedidoId);
        validarPedidoDoCliente(pedido, clienteAutenticadoId);

        if (pedido.getStatus() != PedidoStatus.EM_ANALISE) {
            throw new RegraDeNegocioException("Somente pedidos EM_ANALISE podem ser cancelados.");
        }

        pedido.setStatus(PedidoStatus.CANCELADO);
        return pedidoRepository.update(pedido);
    }

    @Transactional
    public List<Pedido> listarPedidosDoCliente(Long clienteAutenticadoId, UserType userType) {
        validarPerfilCliente(userType);
        return pedidoRepository.findByClienteId(clienteAutenticadoId);
    }

    @Transactional
    public List<Pedido> listarPorStatusParaAgente(String status, Long agenteId, UserType userType) {
        validarPerfilAgente(userType);
        PedidoStatus statusPedido = validarStatus(status);

        if (userType == UserType.BANCO) {
            return pedidoRepository.findByStatusAndTipoPedido(statusPedido, PedidoTipo.COMPRA);
        }

        return pedidoRepository.findByStatusAndTipoPedidoAndAutomovelAgentIdAndAutomovelAgentType(
                statusPedido,
                PedidoTipo.ALUGUEL,
                agenteId,
                mapearAgenteTipo(userType)
        );
    }

    @Transactional
    public Pedido avaliarPedido(Long pedidoId,
                                String resultado,
                                String justificativa,
                                Long agenteId,
                                UserType userType) {
        validarPerfilAgente(userType);

        Pedido pedido = buscarPorId(pedidoId);
        validarPedidoDoAgente(pedido, agenteId, userType);

        if (pedido.getStatus() != PedidoStatus.EM_ANALISE) {
            throw new RegraDeNegocioException("Só é possível avaliar pedido EM_ANALISE.");
        }

        String justificativaNormalizada = justificativa == null ? "" : justificativa.trim();
        if (justificativaNormalizada.isBlank()) {
            throw new RegraDeNegocioException("Justificativa é obrigatória para avaliar pedido.");
        }

        pedido.setStatus(validarResultadoAvaliacao(resultado));
        pedido.setJustificativa(justificativaNormalizada);
        return pedidoRepository.update(pedido);
    }

    public PedidoStatus validarStatus(String status) {
        try {
            return PedidoStatus.fromValue(status);
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Status inválido. Valores aceitos: " + STATUS_VALIDOS + ".");
        }
    }

    public PedidoStatus validarResultadoAvaliacao(String resultado) {
        PedidoStatus status = validarStatus(resultado);
        if (status != PedidoStatus.APROVADO && status != PedidoStatus.REJEITADO) {
            throw new RegraDeNegocioException("Resultado inválido. Valores aceitos: APROVADO, REJEITADO.");
        }
        return status;
    }

    private Pedido cadastrarPedido(Pedido pedido, PedidoTipo tipoPedido) {
        Cliente cliente = buscarCliente(pedido.getCliente().getId());
        Automovel automovel = buscarAutomovel(pedido.getAutomovel().getId());
        validarAutomovelDisponivel(automovel);

        if (tipoPedido == PedidoTipo.COMPRA && pedido.getQntdParcelas() > 1 && automovel.getTaxaJuros() == null) {
            throw new RegraDeNegocioException("Automóvel sem taxaJuros para compra parcelada.");
        }

        pedido.setNumeroProtocolo(gerarNumeroProtocolo());
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setTipoPedido(tipoPedido);
        pedido.setStatus(PedidoStatus.EM_ANALISE);

        return pedidoRepository.save(pedido);
    }

    private static void validarPeriodo(Pedido pedido) {
        if (pedido.getDataInicio() == null || pedido.getDataFim() == null || !pedido.getDataFim().isAfter(pedido.getDataInicio())) {
            throw new RegraDeNegocioException("A dataFim deve ser maior que a dataInicio.");
        }
    }

    private static void validarParcelas(Pedido pedido) {
        if (pedido.getQntdParcelas() == null || pedido.getQntdParcelas() <= 0) {
            throw new RegraDeNegocioException("qntdParcelas deve ser maior que zero.");
        }
    }

    private void validarAutomovelDisponivel(Automovel automovel) {
        if (automovel.getStatus() != AutomovelStatus.DISPONIVEL) {
            throw new RegraDeNegocioException("O automóvel deve estar disponível para criar pedido.");
        }
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));
    }

    private void validarPerfilCliente(UserType userType) {
        if (userType != UserType.CLIENTE) {
            throw new RegraDeNegocioException("Somente cliente pode executar esta operação.");
        }
    }

    private void validarPerfilAgente(UserType userType) {
        if (userType != UserType.EMPRESA && userType != UserType.BANCO) {
            throw new RegraDeNegocioException("Somente empresa ou banco pode executar esta operação.");
        }
    }

    private AgenteTipo mapearAgenteTipo(UserType userType) {
        if (userType == UserType.BANCO) {
            return AgenteTipo.BANCO;
        }
        return AgenteTipo.EMPRESA;
    }

    private void validarPedidoDoCliente(Pedido pedido, Long clienteAutenticadoId) {
        if (!pedido.getCliente().getId().equals(clienteAutenticadoId)) {
            throw new RegraDeNegocioException("Cliente autenticado não pode operar pedido de outro cliente.");
        }
    }

    private void validarPedidoDoAgente(Pedido pedido, Long agenteId, UserType userType) {
        if (userType == UserType.BANCO) {
            if (pedido.getTipoPedido() != PedidoTipo.COMPRA) {
                throw new RegraDeNegocioException("Banco só pode avaliar PedidoCompra.");
            }
            return;
        }

        if (pedido.getTipoPedido() != PedidoTipo.ALUGUEL) {
            throw new RegraDeNegocioException("Empresa só pode avaliar PedidoAluguel.");
        }

        if (!pedido.getAutomovel().getAgentId().equals(agenteId)
                || pedido.getAutomovel().getAgentType() != mapearAgenteTipo(userType)) {
            throw new RegraDeNegocioException("Agente autenticado não pode avaliar pedido de outro agente.");
        }
    }

    private Automovel buscarAutomovel(Long automovelId) {
        return automovelRepository.findById(automovelId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Automóvel não encontrado."));
    }

    private String gerarNumeroProtocolo() {
        String protocolo;
        do {
            protocolo = "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (pedidoRepository.findByNumeroProtocolo(protocolo).isPresent());
        return protocolo;
    }
}

