package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.response.BancoResumoDTO;
import br.pucminas.aluguelcarros.dto.response.ClienteResumoDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoCreditoResponseDTO;
import br.pucminas.aluguelcarros.dto.response.VeiculoResumoDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.service.ContratoCreditoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class ContratoCreditoFacade {

    private final ContratoCreditoService contratoCreditoService;

    @Inject
    public ContratoCreditoFacade(ContratoCreditoService contratoCreditoService) {
        this.contratoCreditoService = contratoCreditoService;
    }

    public ContratoCreditoResponseDTO buscarPorPedidoId(Long pedidoId) {
        return toResponse(contratoCreditoService.buscarPorPedidoId(pedidoId));
    }

    public ContratoCreditoResponseDTO assinar(Long id, Long usuarioId, UserType userType) {
        return toResponse(contratoCreditoService.assinar(id, usuarioId, userType));
    }

    private static ContratoCreditoResponseDTO toResponse(ContratoCredito contratoCredito) {
        Banco banco = contratoCredito.getBanco();
        Cliente cliente = contratoCredito.getCliente();
        Automovel veiculo = contratoCredito.getVeiculo();

        return new ContratoCreditoResponseDTO(
                contratoCredito.getId(),
                new BancoResumoDTO(
                        banco.getId(),
                        banco.getRazaoSocial(),
                        banco.getCnpj(),
                        banco.getCodigoBancario()
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
                contratoCredito.getQuantidadeParcelas(),
                contratoCredito.getValorParcela(),
                contratoCredito.getValorTotal(),
                contratoCredito.getDataAssinaturaBanco(),
                contratoCredito.getDataAssinaturaCliente(),
                contratoCredito.isBancoAssinou(),
                contratoCredito.isClienteAssinou()
        );
    }
}
