package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.dto.request.AssociarCreditoRequestDTO;
import br.pucminas.aluguelcarros.dto.response.AuthMeResponseDTO;
import br.pucminas.aluguelcarros.dto.response.ContratoCreditoResponseDTO;
import br.pucminas.aluguelcarros.enums.UserType;
import br.pucminas.aluguelcarros.exception.RegraDeNegocioException;
import br.pucminas.aluguelcarros.facade.ContratoCreditoFacade;
import br.pucminas.aluguelcarros.service.AuthService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import jakarta.validation.Valid;

@Controller("/banco/pedidos")
public class BancoPedidoController {

    private final ContratoCreditoFacade contratoCreditoFacade;
    private final AuthService authService;

    @Inject
    public BancoPedidoController(ContratoCreditoFacade contratoCreditoFacade, AuthService authService) {
        this.contratoCreditoFacade = contratoCreditoFacade;
        this.authService = authService;
    }

    @Post("/{id}/associar-credito")
    public ContratoCreditoResponseDTO associarCredito(@PathVariable Long id,
                                                      @Body @Valid AssociarCreditoRequestDTO dto,
                                                      HttpRequest<?> request) {
        AuthMeResponseDTO perfil = obterPerfilAutenticado(request);
        validarPerfilBanco(perfil.userType());

        return contratoCreditoFacade.associarCreditoAoPedido(id, dto, perfil.id(), perfil.userType());
    }

    private AuthMeResponseDTO obterPerfilAutenticado(HttpRequest<?> request) {
        String authorization = request.getHeaders().getAuthorization().orElse("");
        return authService.obterPerfilAutenticado(authorization);
    }

    private void validarPerfilBanco(UserType userType) {
        if (userType != UserType.BANCO) {
            throw new RegraDeNegocioException("Somente banco pode associar crédito.");
        }
    }
}

