package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.ContratoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ClienteResumoDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoResponseDTO;
import br.pucminas.aluguelcarros.dto.response.EmpresaResumoDTO;
import br.pucminas.aluguelcarros.dto.response.VeiculoResumoDTO;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.Empresa;
import br.pucminas.aluguelcarros.service.ContratoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ContratoFacade {

    private final ContratoService contratoService;

    @Inject
    public ContratoFacade(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    public ContratoResponseDTO buscarPorPedidoId(Long pedidoId) {
        return toResponse(contratoService.buscarPorPedidoId(pedidoId));
    }

    public ContratoResponseDTO assinar(Long id, Long usuarioId, br.pucminas.aluguelcarros.enums.UserType userType) {
        return toResponse(contratoService.assinar(id, usuarioId, userType));
    }

    private static ContratoResponseDTO toResponse(Contrato contrato) {
        Empresa empresa = contrato.getEmpresa();
        Cliente cliente = contrato.getCliente();
        Automovel veiculo = contrato.getVeiculo();

        return new ContratoResponseDTO(
                contrato.getId(),
                new EmpresaResumoDTO(
                        empresa.getId(),
                        empresa.getRazaoSocial(),
                        empresa.getCnpj(),
                        empresa.getRamoDeAtividade()
                ),
                new ClienteResumoDTO(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getCpf(),
                        cliente.getRg(),
                        cliente.getEndereco(),
                        cliente.getProfissao()
                ),
                new VeiculoResumoDTO(
                        veiculo.getId(),
                        veiculo.getMatricula(),
                        veiculo.getPlaca(),
                        veiculo.getAno(),
                        veiculo.getMarca(),
                        veiculo.getModelo(),
                        veiculo.getValor(),
                        veiculo.getLinkImagem()
                ),
                contrato.getDataInicioAluguel(),
                contrato.getDataFimAluguel(),
                contrato.getValorTotal(),
                contrato.getValorDiaria(),
                contrato.getDataAssinaturaEmpresa(),
                contrato.getDataAssinaturaCliente(),
                contrato.isEmpresaAssinou(),
                contrato.isClienteAssinou()
        );
    }

    public Contrato fromDto(ContratoRequestDTO dto) {
        Contrato contrato = new Contrato();

        Empresa empresa = new Empresa();
        empresa.setId(dto.empresaId());

        Cliente cliente = new Cliente();
        cliente.setId(dto.clienteId());

        Automovel veiculo = new Automovel();
        veiculo.setId(dto.veiculoId());

        contrato.setEmpresa(empresa);
        contrato.setCliente(cliente);
        contrato.setVeiculo(veiculo);
        contrato.setDataInicioAluguel(dto.dataInicioAluguel());
        contrato.setDataFimAluguel(dto.dataFimAluguel());
        contrato.setValorTotal(dto.valorTotal());
        contrato.setValorDiaria(dto.valorDiaria());

        return contrato;
    }
}
