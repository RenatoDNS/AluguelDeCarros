package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.ClienteRequestDTO;
import br.pucminas.aluguelcarros.dto.ClienteResponseDTO;
import br.pucminas.aluguelcarros.dto.EntidadeEmpregadoraDTO;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.EntidadeEmpregadora;
import br.pucminas.aluguelcarros.service.ClienteService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Singleton
public class ClienteFacade {

    private final ClienteService clienteService;

    @Inject
    public ClienteFacade(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        return toResponse(clienteService.cadastrar(fromDto(dto)));
    }

    public ClienteResponseDTO buscar(Long id) {
        return toResponse(clienteService.buscarPorId(id));
    }

    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = fromDto(dto);
        cliente.setId(id);
        return toResponse(clienteService.atualizar(cliente));
    }

    public void remover(Long id) {
        clienteService.deletar(id);
    }

    private static ClienteResponseDTO toResponse(Cliente cliente) {
        return new ClienteResponseDTO(cliente.getId(), cliente.getNome(), cliente.getCpf());
    }

    private static Cliente fromDto(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setRg(dto.getRg());
        cliente.setCpf(dto.getCpf());
        cliente.setNome(dto.getNome());
        cliente.setEndereco(dto.getEndereco());
        cliente.setProfissao(dto.getProfissao());
        cliente.setSenha(dto.getSenha());
        List<EntidadeEmpregadora> lista = new ArrayList<>();
        List<EntidadeEmpregadoraDTO> entidades = dto.getEntidadesEmpregadoras() == null
                ? Collections.emptyList()
                : dto.getEntidadesEmpregadoras();
        for (EntidadeEmpregadoraDTO ed : entidades) {
            EntidadeEmpregadora e = new EntidadeEmpregadora();
            e.setNomeEmpresa(ed.getNomeEmpresa());
            e.setCnpj(ed.getCnpj());
            e.setRendimento(ed.getRendimento());
            lista.add(e);
        }
        cliente.setEntidadesEmpregadoras(lista);
        return cliente;
    }
}
