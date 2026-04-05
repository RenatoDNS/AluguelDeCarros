package br.pucminas.aluguelcarros.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.pucminas.aluguelcarros.exception.EntidadeNaoEncontradaException;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import br.pucminas.aluguelcarros.repository.ClienteRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Inject
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Cliente cadastrar(Cliente cliente) {
        validarEmpregadoras(cliente.getEntidadesEmpregadoras());
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new RegraDeNegocioException("CPF já cadastrado.");
        }
        if (clienteRepository.findByRg(cliente.getRg()).isPresent()) {
            throw new RegraDeNegocioException("RG já cadastrado.");
        }
        cliente.setLogin(cliente.getCpf());
        cliente.setSenha(hashSenha(cliente.getSenha()));
        vincularEmpregadoras(cliente);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));
    }

    @Transactional
    public Cliente atualizar(Cliente cliente) {
        validarEmpregadoras(cliente.getEntidadesEmpregadoras());
        Cliente existente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado."));
        if (!existente.getCpf().equals(cliente.getCpf())
                && clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new RegraDeNegocioException("CPF já cadastrado.");
        }
        if (!existente.getRg().equals(cliente.getRg())
                && clienteRepository.findByRg(cliente.getRg()).isPresent()) {
            throw new RegraDeNegocioException("RG já cadastrado.");
        }
        existente.setRg(cliente.getRg());
        existente.setCpf(cliente.getCpf());
        existente.setNome(cliente.getNome());
        existente.setEndereco(cliente.getEndereco());
        existente.setProfissao(cliente.getProfissao());
        existente.setLogin(cliente.getCpf());
        existente.setSenha(hashSenha(cliente.getSenha()));
        existente.getEntidadesEmpregadoras().clear();
        for (EntidadeEmpregadora e : cliente.getEntidadesEmpregadoras()) {
            e.setId(null);
            e.setCliente(existente);
            existente.getEntidadesEmpregadoras().add(e);
        }
        return clienteRepository.save(existente);
    }

    @Transactional
    public void deletar(Long id) {
        if (clienteRepository.findById(id).isEmpty()) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    private static void validarEmpregadoras(List<EntidadeEmpregadora> lista) {
        if (lista == null || lista.isEmpty() || lista.size() > 3) {
            throw new RegraDeNegocioException("Informe entre 1 e 3 entidades empregadoras.");
        }
    }

    private static void vincularEmpregadoras(Cliente cliente) {
        List<EntidadeEmpregadora> copia = new ArrayList<>(cliente.getEntidadesEmpregadoras());
        cliente.getEntidadesEmpregadoras().clear();
        for (EntidadeEmpregadora e : copia) {
            e.setCliente(cliente);
            cliente.getEntidadesEmpregadoras().add(e);
        }
    }

    private static String hashSenha(String senhaPlana) {
        return BCrypt.withDefaults().hashToString(12, senhaPlana.toCharArray());
    }
}
