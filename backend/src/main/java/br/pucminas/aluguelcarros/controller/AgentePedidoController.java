package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.PedidoAvaliacaoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.PedidoResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.facade.PedidoFacade;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/agente/pedidos")
public class AgentePedidoController {

    private final PedidoFacade pedidoFacade;
    private final AuthService authService;

    @Inject
    public AgentePedidoController(PedidoFacade pedidoFacade,
                                  AuthService authService) {
        this.pedidoFacade = pedidoFacade;
        this.authService = authService;
    }

    @Get("/{status}")
    public List<PedidoResponseDTO> listarPorStatus(@PathVariable String status, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        validarPerfilAgente(perfil.userType());
        return pedidoFacade.listarPorStatusParaAgente(status, perfil.id(), perfil.userType());
    }

    @Post("/{id}/avaliar")
    public PedidoResponseDTO avaliarPedido(@PathVariable Long id,
                                           @Body @Valid PedidoAvaliacaoRequestDTO dto,
                                           HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return pedidoFacade.avaliarPedido(id, dto, perfil.id(), perfil.userType());
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }

    private void validarPerfilAgente(UserType userType) {
        if (userType != UserType.BANCO && userType != UserType.EMPRESA) {
            throw new RegraDeNegocioException("Somente banco ou empresa pode listar pedidos por status.");
        }
    }

}

