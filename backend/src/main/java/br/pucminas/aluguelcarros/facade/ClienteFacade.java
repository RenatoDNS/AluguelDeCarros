package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.ClienteRequestDTO;
import br.pucminas.aluguelcarros.dto.request.EntidadeEmpregadoraRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ClienteResponseDTO;
import br.pucminas.aluguelcarros.dto.response.EntidadeEmpregadoraResponseDTO;
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

    private static ClienteResponseDTO toResponse(Cliente cliente) {
        List<EntidadeEmpregadoraResponseDTO> entidades = cliente.getEntidadesEmpregadoras().stream()
                .map(e -> new EntidadeEmpregadoraResponseDTO(e.getNomeEmpresa(), e.getCnpj(), e.getRendimento()))
                .toList();

        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getRg(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEndereco(),
                cliente.getProfissao(),
                entidades
        );
    }

    private static Cliente fromDto(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setRg(dto.rg());
        cliente.setCpf(dto.cpf());
        cliente.setNome(dto.nome());
        cliente.setEndereco(dto.endereco());
        cliente.setProfissao(dto.profissao());
        cliente.setSenha(dto.senha());
        List<EntidadeEmpregadora> lista = new ArrayList<>();
        List<EntidadeEmpregadoraRequestDTO> entidades = dto.entidadesEmpregadoras() == null
                ? Collections.emptyList()
                : dto.entidadesEmpregadoras();
        for (EntidadeEmpregadoraRequestDTO ed : entidades) {
            EntidadeEmpregadora e = new EntidadeEmpregadora();
            e.setNomeEmpresa(ed.nomeEmpresa());
            e.setCnpj(ed.cnpj());
            e.setRendimento(ed.rendimento());
            lista.add(e);
        }
        cliente.setEntidadesEmpregadoras(lista);
        return cliente;
    }
}
