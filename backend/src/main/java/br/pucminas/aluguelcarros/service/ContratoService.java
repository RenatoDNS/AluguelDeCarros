package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
import br.pucminas.aluguelcarros.enums.TipoPropriedade;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import br.pucminas.aluguelcarros.repository.EmpresaRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Singleton
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final PedidoRepository pedidoRepository;
    private final EmpresaRepository empresaRepository;
    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;

    @Inject
    public ContratoService(ContratoRepository contratoRepository,
                           PedidoRepository pedidoRepository,
                           EmpresaRepository empresaRepository,
                           ClienteRepository clienteRepository,
                           AutomovelRepository automovelRepository) {
        this.contratoRepository = contratoRepository;
        this.pedidoRepository = pedidoRepository;
        this.empresaRepository = empresaRepository;
        this.clienteRepository = clienteRepository;
        this.automovelRepository = automovelRepository;
    }

    @Transactional
    public Contrato buscarPorId(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contrato não encontrado."));
    }

    @Transactional
    public Contrato assinar(Long contratoId, Long usuarioId, UserType userType) {
        Contrato contrato = buscarPorId(contratoId);

        if (userType == UserType.CLIENTE) {
            validarClienteDoContrato(contrato, usuarioId);
            if (!contrato.isClienteAssinou()) {
                contrato.setClienteAssinou(true);
                contrato.setDataAssinaturaCliente(LocalDate.now());
            }
        } else if (userType == UserType.EMPRESA) {
            validarEmpresaDoContrato(contrato, usuarioId);
            if (!contrato.isEmpresaAssinou()) {
                contrato.setEmpresaAssinou(true);
                contrato.setDataAssinaturaEmpresa(LocalDate.now());
            }
        } else {
            throw new RegraDeNegocioException("Somente cliente ou empresa podem assinar contrato de aluguel.");
        }

        if (contrato.isClienteAssinou() && contrato.isEmpresaAssinou()) {
            Automovel veiculo = contrato.getVeiculo();
            veiculo.setStatus(AutomovelStatus.ALUGADO);
            automovelRepository.update(veiculo);
        }

        return contratoRepository.update(contrato);
    }

    @Transactional
    public Contrato obterOuCriarPorPedido(Long pedidoId, TipoPropriedade tipoPropriedade, LocalDate dataAssinatura) {
        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoParaContrato(pedido);

        if (pedido.getAutomovel().getAgentType() != AgenteTipo.EMPRESA) {
            throw new RegraDeNegocioException("Pedido de aluguel deve estar vinculado a um veículo de empresa.");
        }

        Empresa empresa = buscarEmpresa(pedido.getAutomovel().getAgentId());
        Cliente cliente = buscarCliente(pedido.getCliente().getId());
        Automovel veiculo = buscarAutomovel(pedido.getAutomovel().getId());

        LocalDate dataInicio = pedido.getDataInicio();
        LocalDate dataFim = pedido.getDataFim();
        if (dataInicio == null || dataFim == null || !dataFim.isAfter(dataInicio)) {
            throw new RegraDeNegocioException("Pedido de aluguel sem período válido para gerar contrato.");
        }

        Contrato existente = contratoRepository
                .findByClienteIdAndVeiculoIdAndDataInicioAluguelAndDataFimAluguel(
                        cliente.getId(),
                        veiculo.getId(),
                        dataInicio,
                        dataFim
                )
                .orElse(null);
        if (existente != null) {
            return existente;
        }

        BigDecimal valorDiaria = veiculo.getValor();
        long dias = Math.max(1, ChronoUnit.DAYS.between(dataInicio, dataFim));
        BigDecimal valorTotal = valorDiaria.multiply(BigDecimal.valueOf(dias)).setScale(2, RoundingMode.HALF_UP);

        Contrato contrato = new Contrato();
        contrato.setEmpresa(empresa);
        contrato.setCliente(cliente);
        contrato.setVeiculo(veiculo);
        contrato.setDataInicioAluguel(dataInicio);
        contrato.setDataFimAluguel(dataFim);
        contrato.setValorDiaria(valorDiaria);
        contrato.setValorTotal(valorTotal);
        contrato.setEmpresaAssinou(false);
        contrato.setClienteAssinou(false);
        contrato.setDataAssinaturaEmpresa(null);
        contrato.setDataAssinaturaCliente(null);

        return contratoRepository.save(contrato);
    }

    private void validarPedidoParaContrato(Pedido pedido) {
        if (pedido.getStatus() != PedidoStatus.APROVADO || pedido.getTipoPedido() != PedidoTipo.ALUGUEL) {
            throw new RegraDeNegocioException("Contrato só pode ser gerado para pedido de aluguel APROVADO.");
        }
    }

    private void validarClienteDoContrato(Contrato contrato, Long clienteAutenticadoId) {
        if (!contrato.getCliente().getId().equals(clienteAutenticadoId)) {
            throw new RegraDeNegocioException("Cliente autenticado não pode assinar contrato de outro cliente.");
        }
    }

    private void validarEmpresaDoContrato(Contrato contrato, Long empresaAutenticadaId) {
        if (!contrato.getEmpresa().getId().equals(empresaAutenticadaId)) {
            throw new RegraDeNegocioException("Empresa autenticada não pode assinar contrato de outra empresa.");
        }
    }

    private Pedido buscarPedido(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    private Empresa buscarEmpresa(Long empresaId) {
        return empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Empresa não encontrada."));
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));
    }

    private Automovel buscarAutomovel(Long veiculoId) {
        return automovelRepository.findById(veiculoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado."));
    }
}
