package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.AssociarCreditoRequestDTO;
import br.pucminas.aluguelcarros.dto.request.ContratoCreditoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoCreditoResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.service.ContratoCreditoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class ContratoCreditoFacade {

    private final ContratoCreditoService contratoCreditoService;

    @Inject
    public ContratoCreditoFacade(ContratoCreditoService contratoCreditoService) {
        this.contratoCreditoService = contratoCreditoService;
    }

    public ContratoCreditoResponseDTO cadastrar(ContratoCreditoRequestDTO dto, Long bancoAutenticadoId, UserType userType) {
        return toResponse(contratoCreditoService.cadastrar(fromDto(dto), bancoAutenticadoId, userType));
    }

    public ContratoCreditoResponseDTO buscar(Long id) {
        return toResponse(contratoCreditoService.buscarPorId(id));
    }

    public List<ContratoCreditoResponseDTO> listar() {
        return contratoCreditoService.listar().stream().map(ContratoCreditoFacade::toResponse).toList();
    }

    public ContratoCreditoResponseDTO atualizar(Long id,
                                                ContratoCreditoRequestDTO dto,
                                                Long bancoAutenticadoId,
                                                UserType userType) {
        return toResponse(contratoCreditoService.atualizar(id, fromDto(dto), bancoAutenticadoId, userType));
    }

    public void remover(Long id, Long bancoAutenticadoId, UserType userType) {
        contratoCreditoService.deletar(id, bancoAutenticadoId, userType);
    }

    public ContratoCreditoResponseDTO associarCreditoAoPedido(Long pedidoId,
                                                              AssociarCreditoRequestDTO dto,
                                                              Long bancoAutenticadoId,
                                                              UserType userType) {
        return toResponse(contratoCreditoService.associarCreditoAoPedido(
                pedidoId,
                bancoAutenticadoId,
                userType,
                dto.valorFinanciado(),
                dto.taxaJuros(),
                dto.prazoMeses(),
                dto.dataAssinatura()
        ));
    }

    private static ContratoCredito fromDto(ContratoCreditoRequestDTO dto) {
        ContratoCredito contratoCredito = new ContratoCredito();

        Contrato contrato = new Contrato();
        contrato.setId(dto.contratoId());

        contratoCredito.setContrato(contrato);
        contratoCredito.setValorFinanciado(dto.valorFinanciado());
        contratoCredito.setTaxaJuros(dto.taxaJuros());
        contratoCredito.setPrazoMeses(dto.prazoMeses());

        return contratoCredito;
    }

    private static ContratoCreditoResponseDTO toResponse(ContratoCredito contratoCredito) {
        return new ContratoCreditoResponseDTO(
                contratoCredito.getId(),
                contratoCredito.getContrato().getId(),
                contratoCredito.getBanco().getId(),
                contratoCredito.getValorFinanciado(),
                contratoCredito.getTaxaJuros(),
                contratoCredito.getPrazoMeses()
        );
    }
}


