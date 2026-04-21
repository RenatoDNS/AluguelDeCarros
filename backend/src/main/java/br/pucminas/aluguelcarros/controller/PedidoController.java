package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.PedidoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoResponseDTO;
import br.pucminas.aluguelcarros.dto.response.PedidoResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.facade.ContratoFacade;
import br.pucminas.aluguelcarros.facade.PedidoFacade;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

import java.util.List;

@Controller("/pedidos")
public class PedidoController {

    private final PedidoFacade pedidoFacade;
    private final ContratoFacade contratoFacade;
    private final AuthService authService;

    @Inject
    public PedidoController(PedidoFacade pedidoFacade,
                            ContratoFacade contratoFacade,
                            AuthService authService) {
        this.pedidoFacade = pedidoFacade;
        this.contratoFacade = contratoFacade;
        this.authService = authService;
    }

    @Post
    public PedidoResponseDTO cadastrar(@Body @Valid PedidoRequestDTO dto) {
        return pedidoFacade.cadastrar(dto);
    }

    @Get("/me")
    public List<PedidoResponseDTO> listarMe(HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return pedidoFacade.listarMe(perfil.id(), perfil.userType());
    }

    @Post("/{id}/cancelar")
    public PedidoResponseDTO cancelar(@PathVariable Long id, HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        return pedidoFacade.cancelar(id, perfil.id(), perfil.userType());
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }

    private void validarPerfilAgente(UserType userType) {
        if (userType != UserType.BANCO && userType != UserType.EMPRESA) {
            throw new RegraDeNegocioException("Somente empresa ou banco pode executar contrato.");
        }
    }
}

