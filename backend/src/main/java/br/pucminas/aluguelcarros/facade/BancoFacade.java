package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.BancoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.BancoResponseDTO;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.service.BancoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class BancoFacade {

    private final BancoService bancoService;

    @Inject
    public BancoFacade(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    public BancoResponseDTO cadastrar(BancoRequestDTO dto) {
        return toResponse(bancoService.cadastrar(fromDto(dto)));
    }

    private static Banco fromDto(BancoRequestDTO dto) {
        Banco banco = new Banco();
        banco.setRazaoSocial(dto.razaoSocial());
        banco.setCnpj(dto.cnpj());
        banco.setCodigoBancario(dto.codigoBancario());
        banco.setSenha(dto.senha());
        return banco;
    }

    private static BancoResponseDTO toResponse(Banco banco) {
        return new BancoResponseDTO(
                banco.getId(),
                banco.getRazaoSocial(),
                banco.getCnpj(),
                banco.getCodigoBancario()
        );
    }
}

