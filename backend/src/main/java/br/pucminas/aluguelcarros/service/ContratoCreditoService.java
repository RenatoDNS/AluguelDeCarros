package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.model.Pedido;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.BancoRepository;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import br.pucminas.aluguelcarros.repository.ContratoCreditoRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Singleton
public class ContratoCreditoService {

    private final ContratoCreditoRepository contratoCreditoRepository;
    private final BancoRepository bancoRepository;
    private final ClienteRepository clienteRepository;
    private final AutomovelRepository automovelRepository;
    private final PedidoRepository pedidoRepository;

    @Inject
    public ContratoCreditoService(ContratoCreditoRepository contratoCreditoRepository,
                                  BancoRepository bancoRepository,
                                  ClienteRepository clienteRepository,
                                  AutomovelRepository automovelRepository,
                                  PedidoRepository pedidoRepository) {
        this.contratoCreditoRepository = contratoCreditoRepository;
        this.bancoRepository = bancoRepository;
        this.clienteRepository = clienteRepository;
        this.automovelRepository = automovelRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ContratoCredito buscarPorId(Long id) {
        return contratoCreditoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contrato de crédito não encontrado."));
    }

    @Transactional
    public ContratoCredito buscarPorPedidoId(Long pedidoId) {
        Pedido pedido = buscarPedido(pedidoId);

        if (pedido.getTipoPedido() != PedidoTipo.COMPRA) {
            throw new RegraDeNegocioException("Pedido informado não é do tipo compra.");
        }

        return contratoCreditoRepository
                .findByClienteIdAndVeiculoId(pedido.getCliente().getId(), pedido.getAutomovel().getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contrato de crédito não encontrado para o pedido informado."));
    }

    @Transactional
    public ContratoCredito assinar(Long contratoId, Long usuarioId, UserType userType) {
        ContratoCredito contratoCredito = buscarPorId(contratoId);

        if (userType == UserType.CLIENTE) {
            validarClienteDoContrato(contratoCredito, usuarioId);
            if (!contratoCredito.isClienteAssinou()) {
                contratoCredito.setClienteAssinou(true);
                contratoCredito.setDataAssinaturaCliente(LocalDate.now());
            }
        } else if (userType == UserType.BANCO) {
            validarBancoDoContrato(contratoCredito, usuarioId);
            if (!contratoCredito.isBancoAssinou()) {
                contratoCredito.setBancoAssinou(true);
                contratoCredito.setDataAssinaturaBanco(LocalDate.now());
            }
        } else {
            throw new RegraDeNegocioException("Somente cliente ou banco podem assinar contrato de crédito.");
        }

        if (contratoCredito.isClienteAssinou() && contratoCredito.isBancoAssinou()) {
            Automovel veiculo = contratoCredito.getVeiculo();
            veiculo.setStatus(AutomovelStatus.VINCULADO);
            automovelRepository.update(veiculo);
        }

        return contratoCreditoRepository.update(contratoCredito);
    }

    @Transactional
    public ContratoCredito obterOuCriarPorPedido(Long pedidoId,
                                                 Long bancoAutenticadoId,
                                                 UserType userType,
                                                 Double valorFinanciado,
                                                 Double taxaJuros,
                                                 Integer prazoMeses,
                                                 LocalDate dataAssinatura) {
        validarPerfilBanco(userType);

        Pedido pedido = buscarPedido(pedidoId);
        validarPedidoParaContratoCredito(pedido);

        Banco banco = buscarBanco(bancoAutenticadoId);
        Cliente cliente = buscarCliente(pedido.getCliente().getId());
        Automovel veiculo = buscarVeiculo(pedido.getAutomovel().getId());

        ContratoCredito existente = contratoCreditoRepository
                .findByBancoIdAndClienteIdAndVeiculoId(banco.getId(), cliente.getId(), veiculo.getId())
                .orElse(null);
        if (existente != null) {
            return existente;
        }

        int parcelas = prazoMeses == null || prazoMeses <= 0 ? 1 : prazoMeses;
        BigDecimal valorBase = BigDecimal.valueOf(valorFinanciado == null ? 0D : valorFinanciado);
        BigDecimal juros = BigDecimal.valueOf(taxaJuros == null ? 0D : taxaJuros);

        BigDecimal total = valorBase.multiply(BigDecimal.ONE.add(juros)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorParcela = total.divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.HALF_UP);

        ContratoCredito contratoCredito = new ContratoCredito();
        contratoCredito.setBanco(banco);
        contratoCredito.setCliente(cliente);
        contratoCredito.setVeiculo(veiculo);
        contratoCredito.setQuantidadeParcelas(parcelas);
        contratoCredito.setValorParcela(valorParcela);
        contratoCredito.setValorTotal(total);
        contratoCredito.setBancoAssinou(false);
        contratoCredito.setClienteAssinou(false);
        contratoCredito.setDataAssinaturaBanco(null);
        contratoCredito.setDataAssinaturaCliente(null);

        return contratoCreditoRepository.save(contratoCredito);
    }

    private void validarPerfilBanco(UserType userType) {
        if (userType != UserType.BANCO) {
            throw new RegraDeNegocioException("Somente banco pode associar crédito.");
        }
    }

    private void validarPedidoParaContratoCredito(Pedido pedido) {
        if (pedido.getStatus() != PedidoStatus.APROVADO || pedido.getTipoPedido() != PedidoTipo.COMPRA) {
            throw new RegraDeNegocioException("Contrato de crédito só pode ser gerado para pedido de compra APROVADO.");
        }
    }

    private void validarClienteDoContrato(ContratoCredito contratoCredito, Long clienteAutenticadoId) {
        if (!contratoCredito.getCliente().getId().equals(clienteAutenticadoId)) {
            throw new RegraDeNegocioException("Cliente autenticado não pode assinar contrato de outro cliente.");
        }
    }

    private void validarBancoDoContrato(ContratoCredito contratoCredito, Long bancoAutenticadoId) {
        if (!contratoCredito.getBanco().getId().equals(bancoAutenticadoId)) {
            throw new RegraDeNegocioException("Banco autenticado não pode assinar contrato de outro banco.");
        }
    }

    private Pedido buscarPedido(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Pedido não encontrado."));
    }

    private Banco buscarBanco(Long bancoId) {
        return bancoRepository.findById(bancoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado."));
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));
    }

    private Automovel buscarVeiculo(Long veiculoId) {
        return automovelRepository.findById(veiculoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado."));
    }
}
