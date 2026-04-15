package br.pucminas.aluguelcarros.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;

import java.util.Map;

@Singleton
public class GlobalExceptionHandler {

    @Error(global = true, exception = EntidadeNaoEncontradaException.class)
    @Produces
    public HttpResponse<Map<String, String>> handleNaoEncontrado(
            HttpRequest<?> request,
            EntidadeNaoEncontradaException ex) {
        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", ex.getMensagem()));
    }

    @Error(global = true, exception = RegraDeNegocioException.class)
    @Produces
    public HttpResponse<Map<String, String>> handleRegraDeNegocio(
            HttpRequest<?> request,
            RegraDeNegocioException ex) {
        return HttpResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("mensagem", ex.getMensagem()));
    }

    @Error(global = true, exception = ConstraintViolationException.class)
    @Produces
    public HttpResponse<Map<String, String>> handleValidacao(
            HttpRequest<?> request,
            ConstraintViolationException ex) {
        return HttpResponse.badRequest()
                .body(Map.of("mensagem", "Dados inválidos."));
    }
}
