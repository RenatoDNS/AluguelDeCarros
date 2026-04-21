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

