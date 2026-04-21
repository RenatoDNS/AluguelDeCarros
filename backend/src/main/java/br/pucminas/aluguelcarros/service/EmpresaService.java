package br.pucminas.aluguelcarros.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.repository.BancoRepository;
import br.pucminas.aluguelcarros.repository.EmpresaRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final BancoRepository bancoRepository;

    @Inject
    public EmpresaService(EmpresaRepository empresaRepository, BancoRepository bancoRepository) {
        this.empresaRepository = empresaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Transactional
    public Empresa cadastrar(Empresa empresa) {
        String cnpj = normalizarCnpj(empresa.getCnpj());
        validarCnpjDisponivel(cnpj, null);

        empresa.setCnpj(cnpj);
        empresa.setRazaoSocial(normalizarTexto(empresa.getRazaoSocial()));
        empresa.setRamoDeAtividade(normalizarTexto(empresa.getRamoDeAtividade()));
        empresa.setLogin(cnpj);
        empresa.setSenha(hashSenha(empresa.getSenha()));

        return empresaRepository.save(empresa);
    }

    private void validarCnpjDisponivel(String cnpj, Long idAtual) {
        empresaRepository.findByCnpj(cnpj)
                .filter(e -> idAtual == null || !e.getId().equals(idAtual))
                .ifPresent(e -> {
                    throw new RegraDeNegocioException("CNPJ já cadastrado para empresa.");
                });

        if (bancoRepository.findByCnpj(cnpj).isPresent()) {
            throw new RegraDeNegocioException("CNPJ já cadastrado para banco.");
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

