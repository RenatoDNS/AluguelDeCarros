package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.PedidoAluguelRequestDTO;
import br.pucminas.aluguelcarros.dto.request.PedidoCompraRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.PedidoResponseDTO;
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

@Controller("/pedidos")
public class PedidoController {

    private final PedidoFacade pedidoFacade;
    private final AuthService authService;

    @Inject
    public PedidoController(PedidoFacade pedidoFacade,
                            AuthService authService) {
        this.pedidoFacade = pedidoFacade;
        this.authService = authService;
    }

    @Post("/aluguel")
    public PedidoResponseDTO cadastrarAluguel(@Body @Valid PedidoAluguelRequestDTO dto) {
        return pedidoFacade.cadastrarAluguel(dto);
    }

    @Post("/credito")
    public PedidoResponseDTO cadastrarCompra(@Body @Valid PedidoCompraRequestDTO dto) {
        return pedidoFacade.cadastrarCompra(dto);
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
}

