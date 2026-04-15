package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.enums.TipoPropriedade;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.repository.BancoRepository;
import br.pucminas.aluguelcarros.repository.ContratoCreditoRepository;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ContratoCreditoService {

    private final ContratoCreditoRepository contratoCreditoRepository;
    private final ContratoRepository contratoRepository;
    private final BancoRepository bancoRepository;
    private final ContratoService contratoService;

    @Inject
    public ContratoCreditoService(ContratoCreditoRepository contratoCreditoRepository,
                                  ContratoRepository contratoRepository,
                                  BancoRepository bancoRepository,
                                  ContratoService contratoService) {
        this.contratoCreditoRepository = contratoCreditoRepository;
        this.contratoRepository = contratoRepository;
        this.bancoRepository = bancoRepository;
        this.contratoService = contratoService;
    }

    @Transactional
    public ContratoCredito cadastrar(ContratoCredito contratoCredito, Long bancoAutenticadoId, UserType userType) {
        validarPerfilBanco(userType);

        Contrato contrato = buscarContrato(contratoCredito.getContrato().getId());
        Banco banco = buscarBanco(bancoAutenticadoId);

        validarContratoUnico(contrato.getId(), null);

        contratoCredito.setContrato(contrato);
        contratoCredito.setBanco(banco);

        return contratoCreditoRepository.save(contratoCredito);
    }

    @Transactional
    public ContratoCredito buscarPorId(Long id) {
        return contratoCreditoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contrato de crédito não encontrado."));
    }

    @Transactional
    public List<ContratoCredito> listar() {
        List<ContratoCredito> contratosCredito = new ArrayList<>();
        contratoCreditoRepository.findAll().forEach(contratosCredito::add);
        return contratosCredito;
    }

    @Transactional
    public ContratoCredito atualizar(Long id,
                                     ContratoCredito atualizacao,
                                     Long bancoAutenticadoId,
                                     UserType userType) {
        validarPerfilBanco(userType);

        ContratoCredito existente = buscarPorId(id);
        validarBancoProprietario(existente, bancoAutenticadoId);
        Contrato contrato = buscarContrato(atualizacao.getContrato().getId());
        Banco banco = buscarBanco(bancoAutenticadoId);

        validarContratoUnico(contrato.getId(), existente.getId());

        existente.setContrato(contrato);
        existente.setBanco(banco);
        existente.setValorFinanciado(atualizacao.getValorFinanciado());
        existente.setTaxaJuros(atualizacao.getTaxaJuros());
        existente.setPrazoMeses(atualizacao.getPrazoMeses());

        return contratoCreditoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id, Long bancoAutenticadoId, UserType userType) {
        validarPerfilBanco(userType);
        ContratoCredito contratoCredito = buscarPorId(id);
        validarBancoProprietario(contratoCredito, bancoAutenticadoId);
        contratoCreditoRepository.delete(contratoCredito);
    }

    @Transactional
    public ContratoCredito associarCreditoAoPedido(Long pedidoId,
                                                   Long bancoAutenticadoId,
                                                   UserType userType,
                                                   Double valorFinanciado,
                                                   Double taxaJuros,
                                                   Integer prazoMeses,
                                                   LocalDate dataAssinatura) {
        validarPerfilBanco(userType);

        Contrato contrato = contratoService.obterOuCriarPorPedido(pedidoId, TipoPropriedade.BANCO, dataAssinatura);
        validarContratoUnico(contrato.getId(), null);
        Banco banco = buscarBanco(bancoAutenticadoId);

        ContratoCredito contratoCredito = new ContratoCredito();
        contratoCredito.setContrato(contrato);
        contratoCredito.setBanco(banco);
        contratoCredito.setValorFinanciado(valorFinanciado);
        contratoCredito.setTaxaJuros(taxaJuros);
        contratoCredito.setPrazoMeses(prazoMeses);

        return contratoCreditoRepository.save(contratoCredito);
    }

    private void validarPerfilBanco(UserType userType) {
        if (userType != UserType.BANCO) {
            throw new RegraDeNegocioException("Somente banco pode associar crédito.");
        }
    }

    private void validarContratoUnico(Long contratoId, Long idAtual) {
        contratoCreditoRepository.findByContratoId(contratoId)
                .filter(item -> idAtual == null || !item.getId().equals(idAtual))
                .ifPresent(item -> {
                    throw new RegraDeNegocioException("Já existe contrato de crédito para este contrato.");
                });
    }

    private void validarBancoProprietario(ContratoCredito contratoCredito, Long bancoAutenticadoId) {
        if (!contratoCredito.getBanco().getId().equals(bancoAutenticadoId)) {
            throw new RegraDeNegocioException("Somente o banco associado pode alterar este contrato de crédito.");
        }
    }

    private Contrato buscarContrato(Long contratoId) {
        return contratoRepository.findById(contratoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Contrato não encontrado."));
    }

    private Banco buscarBanco(Long bancoId) {
        return bancoRepository.findById(bancoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado."));
    }
}


