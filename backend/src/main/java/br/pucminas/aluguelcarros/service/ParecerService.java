package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.ParecerResultado;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.model.Parecer;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.repository.BancoRepository;
import br.pucminas.aluguelcarros.repository.EmpresaRepository;
import br.pucminas.aluguelcarros.repository.ParecerRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ParecerService {

    private static final String RESULTADOS_VALIDOS = "APROVADO, REJEITADO";

    private final ParecerRepository parecerRepository;
    private final PedidoRepository pedidoRepository;
    private final BancoRepository bancoRepository;
    private final EmpresaRepository empresaRepository;

    @Inject
    public ParecerService(ParecerRepository parecerRepository,
                          PedidoRepository pedidoRepository,
                          BancoRepository bancoRepository,
                          EmpresaRepository empresaRepository) {
        this.parecerRepository = parecerRepository;
        this.pedidoRepository = pedidoRepository;
        this.bancoRepository = bancoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Parecer cadastrar(Parecer parecer, AgenteTipo agenteTipo, Long agenteId) {
        Pedido pedido = pedidoRepository.findById(parecer.getPedido().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));

        if (pedido.getStatus() != PedidoStatus.EM_ANALISE) {
            throw new RegraDeNegocioException("Só é possível emitir parecer para pedido EM_ANALISE.");
        }

        if (parecerRepository.findByPedidoId(pedido.getId()).isPresent()) {
            throw new RegraDeNegocioException("Já existe parecer emitido para este pedido.");
        }

        parecer.setPedido(pedido);
        vincularAgente(parecer, agenteTipo, agenteId);

        Parecer salvo = parecerRepository.save(parecer);
        atualizarStatusPedido(pedido, salvo.getResultado());
        return salvo;
    }

    @Transactional
    public Parecer avaliarPedido(Long pedidoId,
                                 AgenteTipo agenteTipo,
                                 Long agenteId,
                                 ParecerResultado resultado,
                                 String justificativa) {
        Parecer parecer = new Parecer();
        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);

        parecer.setPedido(pedido);
        parecer.setResultado(resultado);
        parecer.setJustificativa(justificativa == null ? "" : justificativa.trim());
        parecer.setDataEmissao(LocalDate.now());

        return cadastrar(parecer, agenteTipo, agenteId);
    }

    @Transactional
    public Parecer buscarPorId(Long id) {
        return parecerRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Parecer não encontrado."));
    }

    @Transactional
    public List<Parecer> listar() {
        List<Parecer> pareceres = new ArrayList<>();
        parecerRepository.findAll().forEach(pareceres::add);
        return pareceres;
    }

    @Transactional
    public Parecer atualizar(Long id, Parecer atualizacao, AgenteTipo agenteTipo, Long agenteId) {
        Parecer existente = parecerRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Parecer não encontrado."));

        Pedido pedido = pedidoRepository.findById(atualizacao.getPedido().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));

        if (!pedido.getId().equals(existente.getPedido().getId()) && pedido.getStatus() != PedidoStatus.EM_ANALISE) {
            throw new RegraDeNegocioException("Só é possível emitir parecer para pedido EM_ANALISE.");
        }

        if (!pedido.getId().equals(existente.getPedido().getId())
                && parecerRepository.findByPedidoId(pedido.getId()).isPresent()) {
            throw new RegraDeNegocioException("Já existe parecer emitido para este pedido.");
        }

        existente.setPedido(pedido);
        existente.setResultado(atualizacao.getResultado());
        existente.setJustificativa(atualizacao.getJustificativa().trim());
        existente.setDataEmissao(atualizacao.getDataEmissao());
        vincularAgente(existente, agenteTipo, agenteId);

        Parecer salvo = parecerRepository.save(existente);
        atualizarStatusPedido(pedido, salvo.getResultado());
        return salvo;
    }

    @Transactional
    public void deletar(Long id) {
        Parecer parecer = parecerRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Parecer não encontrado."));

        Pedido pedido = parecer.getPedido();
        parecerRepository.delete(parecer);

        pedido.setStatus(PedidoStatus.EM_ANALISE);
        pedidoRepository.update(pedido);
    }

    public ParecerResultado validarResultado(String resultado) {
        try {
            return ParecerResultado.fromValue(resultado);
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Resultado inválido. Valores aceitos: " + RESULTADOS_VALIDOS + ".");
        }
    }

    public AgenteTipo validarAgenteTipo(String agenteTipo) {
        try {
            return AgenteTipo.fromValue(agenteTipo);
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("agenteTipo inválido. Valores aceitos: BANCO, EMPRESA.");
        }
    }

    private void vincularAgente(Parecer parecer, AgenteTipo agenteTipo, Long agenteId) {
        if (agenteTipo == AgenteTipo.BANCO) {
            Banco banco = bancoRepository.findById(agenteId)
                    .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado."));
            parecer.setBanco(banco);
            parecer.setEmpresa(null);
            return;
        }

        Empresa empresa = empresaRepository.findById(agenteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa não encontrada."));
        parecer.setEmpresa(empresa);
        parecer.setBanco(null);
    }

    private void atualizarStatusPedido(Pedido pedido, ParecerResultado resultado) {
        if (resultado == ParecerResultado.APROVADO) {
            pedido.setStatus(PedidoStatus.APROVADO);
        } else {
            pedido.setStatus(PedidoStatus.REJEITADO);
        }
        pedidoRepository.update(pedido);
    }
}


