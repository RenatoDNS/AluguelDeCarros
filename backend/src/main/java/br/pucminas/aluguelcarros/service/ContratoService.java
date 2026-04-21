package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.TipoPropriedade;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ContratoService {

    private static final String TIPOS_VALIDOS = "CLIENTE, EMPRESA, BANCO";

    private final ContratoRepository contratoRepository;
    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;

    @Inject
    public ContratoService(ContratoRepository contratoRepository,
                           PedidoRepository pedidoRepository,
                           AutomovelRepository automovelRepository) {
        this.contratoRepository = contratoRepository;
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
    }

    @Transactional
    public Contrato cadastrar(Contrato contrato) {
        Pedido pedido = buscarPedido(contrato.getPedido().getId());
        validarPedidoAprovado(pedido);
        validarContratoUnicoPorPedido(pedido.getId(), null);

        contrato.setPedido(pedido);
        return contratoRepository.save(contrato);
    }

    @Transactional
    public Contrato buscarPorId(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contrato não encontrado."));
    }

    @Transactional
    public List<Contrato> listar() {
        List<Contrato> contratos = new ArrayList<>();
        contratoRepository.findAll().forEach(contratos::add);
        return contratos;
    }

    @Transactional
    public Contrato atualizar(Contrato contrato) {
        Contrato existente = buscarPorId(contrato.getId());

        Pedido pedido = buscarPedido(contrato.getPedido().getId());
        validarPedidoAprovado(pedido);
        validarContratoUnicoPorPedido(pedido.getId(), existente.getId());

        existente.setPedido(pedido);
        existente.setTipoPropriedade(contrato.getTipoPropriedade());
        existente.setDataAssinatura(contrato.getDataAssinatura());

        return contratoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Contrato contrato = buscarPorId(id);
        contratoRepository.delete(contrato);
    }

    @Transactional
    public Contrato executar(Long id) {
        Contrato contrato = buscarPorId(id);
        Pedido pedido = buscarPedido(contrato.getPedido().getId());
        validarPedidoAprovado(pedido);

        Automovel automovel = pedido.getAutomovel();
        automovel.setStatus(obterStatusAutomovelExecucao(contrato.getTipoPropriedade()));
        automovelRepository.update(automovel);

        return contrato;
    }

    @Transactional
    public Contrato obterOuCriarPorPedido(Long pedidoId, TipoPropriedade tipoPropriedade, LocalDate dataAssinatura) {
        Contrato existente = contratoRepository.findByPedidoId(pedidoId).orElse(null);
        if (existente != null) {
            if (existente.getTipoPropriedade() != tipoPropriedade) {
                throw new RegraDeNegocioException("Pedido já possui contrato com tipoPropriedade diferente.");
            }
            return existente;
        }

        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoAprovado(pedido);

        Contrato contrato = new Contrato();
        contrato.setPedido(pedido);
        contrato.setTipoPropriedade(tipoPropriedade);
        contrato.setDataAssinatura(dataAssinatura == null ? LocalDate.now() : dataAssinatura);
        return contratoRepository.save(contrato);
    }

    public TipoPropriedade validarTipoPropriedade(String tipoPropriedade) {
        try {
            return TipoPropriedade.fromValue(tipoPropriedade);
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("tipoPropriedade inválido. Valores aceitos: " + TIPOS_VALIDOS + ".");
        }
    }

    private void validarPedidoAprovado(Pedido pedido) {
        if (pedido.getStatus() != PedidoStatus.APROVADO) {
            throw new RegraDeNegocioException("Contrato só pode ser criado ou executado para pedido APROVADO.");
        }
    }

    private void validarContratoUnicoPorPedido(Long pedidoId, Long contratoIdAtual) {
        contratoRepository.findByPedidoId(pedidoId)
                .filter(contrato -> contratoIdAtual == null || !contrato.getId().equals(contratoIdAtual))
                .ifPresent(contrato -> {
                    throw new RegraDeNegocioException("Já existe contrato para este pedido.");
                });
    }

    private Pedido buscarPedido(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    private AutomovelStatus obterStatusAutomovelExecucao(TipoPropriedade tipoPropriedade) {
        if (tipoPropriedade == TipoPropriedade.CLIENTE) {
            return AutomovelStatus.ALUGADO;
        }
        return AutomovelStatus.VINCULADO;
    }
}

