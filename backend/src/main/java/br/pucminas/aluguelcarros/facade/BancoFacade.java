package br.pucminas.aluguelcarros.facade;

import br.pucminas.aluguelcarros.dto.request.BancoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.BancoResponseDTO;
import br.pucminas.aluguelcarros.model.Banco;
import br.pucminas.aluguelcarros.service.BancoService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;

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

    public BancoResponseDTO buscar(Long id) {
        return toResponse(bancoService.buscarPorId(id));
    }

    public List<BancoResponseDTO> listar() {
        return bancoService.listar().stream().map(BancoFacade::toResponse).toList();
    }

    public BancoResponseDTO atualizar(Long id, BancoRequestDTO dto) {
        Banco banco = fromDto(dto);
        banco.setId(id);
        return toResponse(bancoService.atualizar(banco));
    }

    public void remover(Long id) {
        bancoService.deletar(id);
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

