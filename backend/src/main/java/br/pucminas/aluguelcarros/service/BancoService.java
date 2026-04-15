package br.pucminas.aluguelcarros.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.repository.BancoRepository;
import br.pucminas.aluguelcarros.repository.EmpresaRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class BancoService {

    private final BancoRepository bancoRepository;
    private final EmpresaRepository empresaRepository;

    @Inject
    public BancoService(BancoRepository bancoRepository, EmpresaRepository empresaRepository) {
        this.bancoRepository = bancoRepository;
        this.empresaRepository = empresaRepository;
    }

    @Transactional
    public Banco cadastrar(Banco banco) {
        String cnpj = normalizarCnpj(banco.getCnpj());
        validarCnpjDisponivel(cnpj, null);

        banco.setCnpj(cnpj);
        banco.setRazaoSocial(normalizarTexto(banco.getRazaoSocial()));
        banco.setCodigoBancario(normalizarTexto(banco.getCodigoBancario()));
        banco.setLogin(cnpj);
        banco.setSenha(hashSenha(banco.getSenha()));

        return bancoRepository.save(banco);
    }

    @Transactional
    public Banco buscarPorId(Long id) {
        return bancoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado."));
    }

    @Transactional
    public List<Banco> listar() {
        List<Banco> bancos = new ArrayList<>();
        bancoRepository.findAll().forEach(bancos::add);
        return bancos;
    }

    @Transactional
    public Banco atualizar(Banco banco) {
        Banco existente = bancoRepository.findById(banco.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado."));

        String cnpj = normalizarCnpj(banco.getCnpj());
        validarCnpjDisponivel(cnpj, existente.getId());

        existente.setRazaoSocial(normalizarTexto(banco.getRazaoSocial()));
        existente.setCnpj(cnpj);
        existente.setCodigoBancario(normalizarTexto(banco.getCodigoBancario()));
        existente.setLogin(cnpj);
        existente.setSenha(hashSenha(banco.getSenha()));

        return bancoRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Banco não encontrado."));
        bancoRepository.delete(banco);
    }

    private void validarCnpjDisponivel(String cnpj, Long idAtual) {
        bancoRepository.findByCnpj(cnpj)
                .filter(b -> idAtual == null || !b.getId().equals(idAtual))
                .ifPresent(b -> {
                    throw new RegraDeNegocioException("CNPJ já cadastrado para banco.");
                });

        if (empresaRepository.findByCnpj(cnpj).isPresent()) {
            throw new RegraDeNegocioException("CNPJ já cadastrado para empresa.");
        }
    }

    private static String normalizarCnpj(String cnpj) {
        return normalizarTexto(cnpj).replaceAll("\\D", "");
    }

    private static String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private static String hashSenha(String senhaPlana) {
        return BCrypt.withDefaults().hashToString(12, senhaPlana.toCharArray());
    }
}

