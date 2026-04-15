package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
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

import java.util.ArrayList;
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
    public Pedido cadastrar(Pedido pedido) {
        validarPeriodo(pedido);

        Cliente cliente = buscarCliente(pedido.getCliente().getId());
        Automovel automovel = buscarAutomovel(pedido.getAutomovel().getId());
        validarAutomovelDisponivel(automovel);

        pedido.setNumeroProtocolo(gerarNumeroProtocolo());
        pedido.setCliente(cliente);
        pedido.setAutomovel(automovel);
        pedido.setStatus(PedidoStatus.EM_ANALISE);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    @Transactional
    public List<Pedido> listar() {
        List<Pedido> pedidos = new ArrayList<>();
        pedidoRepository.findAll().forEach(pedidos::add);
        return pedidos;
    }

    @Transactional
    public List<Pedido> listarPorStatus(String status) {
        return pedidoRepository.findByStatus(validarStatus(status));
    }

    @Transactional
    public Pedido atualizar(Pedido pedido) {
        Pedido existente = pedidoRepository.findById(pedido.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));

        validarPeriodo(pedido);

        Cliente cliente = buscarCliente(pedido.getCliente().getId());
        Automovel automovel = buscarAutomovel(pedido.getAutomovel().getId());

        existente.setCliente(cliente);
        existente.setAutomovel(automovel);
        existente.setDataInicio(pedido.getDataInicio());
        existente.setDataFim(pedido.getDataFim());

        if (pedido.getStatus() != null) {
            existente.setStatus(pedido.getStatus());
        }

        return pedidoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
        pedidoRepository.delete(pedido);
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
    public List<Pedido> listarEmAnaliseParaAgente(UserType userType) {
        validarPerfilAgente(userType);
        return pedidoRepository.findByStatus(PedidoStatus.EM_ANALISE);
    }

    public PedidoStatus validarStatus(String status) {
        try {
            return PedidoStatus.fromValue(status);
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Status inválido. Valores aceitos: " + STATUS_VALIDOS + ".");
        }
    }

    private static void validarPeriodo(Pedido pedido) {
        if (pedido.getDataInicio() == null || pedido.getDataFim() == null || !pedido.getDataFim().isAfter(pedido.getDataInicio())) {
            throw new RegraDeNegocioException("A dataFim deve ser maior que a dataInicio.");
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

    private void validarPedidoDoCliente(Pedido pedido, Long clienteAutenticadoId) {
        if (!pedido.getCliente().getId().equals(clienteAutenticadoId)) {
            throw new RegraDeNegocioException("Cliente autenticado não pode operar pedido de outro cliente.");
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

