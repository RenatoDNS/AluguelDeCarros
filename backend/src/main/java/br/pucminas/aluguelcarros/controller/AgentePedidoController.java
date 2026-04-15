package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.PedidoAvaliacaoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.ParecerResponseDTO;
import br.pucminas.aluguelcarros.dto.response.PedidoResponseDTO;
import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.facade.ParecerFacade;
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
    private final ParecerFacade parecerFacade;
    private final AuthService authService;

    @Inject
    public AgentePedidoController(PedidoFacade pedidoFacade,
                                  ParecerFacade parecerFacade,
                                  AuthService authService) {
        this.pedidoFacade = pedidoFacade;
        this.parecerFacade = parecerFacade;
        this.authService = authService;
    }

    @Get("/em-analise")
    public List<PedidoResponseDTO> listarEmAnalise(HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return pedidoFacade.listarEmAnaliseParaAgente(perfil.id(), perfil.userType());
    }

    @Post("/{id}/avaliar")
    public ParecerResponseDTO avaliarPedido(@PathVariable Long id,
                                            @Body @Valid PedidoAvaliacaoRequestDTO dto,
                                            HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return parecerFacade.avaliarPedido(id, dto, mapearAgenteTipo(perfil.userType()), perfil.id());
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }

    private AgenteTipo mapearAgenteTipo(UserType userType) {
        if (userType == UserType.BANCO) {
            return AgenteTipo.BANCO;
        }
        if (userType == UserType.EMPRESA) {
            return AgenteTipo.EMPRESA;
        }
        throw new RegraDeNegocioException("Somente empresa ou banco pode avaliar pedidos.");
    }
}

