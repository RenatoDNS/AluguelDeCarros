package br.pucminas.aluguelcarros.service;

import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class AutomovelService {

    private static final String STATUS_VALIDOS = "DISPONIVEL, ALUGADO, VINCULADO";

    private final AutomovelRepository automovelRepository;

    @Inject
    public AutomovelService(AutomovelRepository automovelRepository) {
        this.automovelRepository = automovelRepository;
    }

    @Transactional
    public Automovel cadastrar(Automovel automovel) {
        String matricula = normalizarTexto(automovel.getMatricula());
        String placa = normalizarPlaca(automovel.getPlaca());

        validarMatriculaDisponivel(matricula, null);
        validarPlacaDisponivel(placa, null);

        automovel.setMatricula(matricula);
        automovel.setPlaca(placa);
        automovel.setMarca(normalizarTexto(automovel.getMarca()));
        automovel.setModelo(normalizarTexto(automovel.getModelo()));
        automovel.setStatus(validarStatus(automovel.getStatus().name()));

        return automovelRepository.save(automovel);
    }

    @Transactional
    public Automovel buscarPorId(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Automovel nao encontrado."));
    }

    @Transactional
    public List<Automovel> listar() {
        List<Automovel> automoveis = new ArrayList<>();
        automovelRepository.findAll().forEach(automoveis::add);
        return automoveis;
    }

    @Transactional
    public List<Automovel> listarPorStatus(String status) {
        AutomovelStatus automovelStatus = validarStatus(status);
        return automovelRepository.findByStatus(automovelStatus);
    }

    @Transactional
    public Automovel atualizar(Automovel automovel) {
        Automovel existente = automovelRepository.findById(automovel.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Automovel nao encontrado."));

        String matricula = normalizarTexto(automovel.getMatricula());
        String placa = normalizarPlaca(automovel.getPlaca());

        validarMatriculaDisponivel(matricula, existente.getId());
        validarPlacaDisponivel(placa, existente.getId());

        existente.setMatricula(matricula);
        existente.setPlaca(placa);
        existente.setAno(automovel.getAno());
        existente.setMarca(normalizarTexto(automovel.getMarca()));
        existente.setModelo(normalizarTexto(automovel.getModelo()));
        existente.setStatus(validarStatus(automovel.getStatus().name()));

        return automovelRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        Automovel automovel = automovelRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Automovel nao encontrado."));
        automovelRepository.delete(automovel);
    }

    public AutomovelStatus validarStatus(String status) {
        try {
            return AutomovelStatus.fromValue(status);
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Status invalido. Valores aceitos: " + STATUS_VALIDOS + ".");
        }
    }

    private void validarMatriculaDisponivel(String matricula, Long idAtual) {
        automovelRepository.findByMatricula(matricula)
                .filter(a -> idAtual == null || !a.getId().equals(idAtual))
                .ifPresent(a -> {
                    throw new RegraDeNegocioException("Matricula ja cadastrada.");
                });
    }

    private void validarPlacaDisponivel(String placa, Long idAtual) {
        automovelRepository.findByPlaca(placa)
                .filter(a -> idAtual == null || !a.getId().equals(idAtual))
                .ifPresent(a -> {
                    throw new RegraDeNegocioException("Placa ja cadastrada.");
                });
    }

    private static String normalizarPlaca(String placa) {
        return normalizarTexto(placa).toUpperCase();
    }

    private static String normalizarTexto(String valor) {
        return valor == null ? "" : valor.trim();
    }
}

