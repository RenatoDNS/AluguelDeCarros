package br.pucminas.aluguelcarros.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Produces;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class GlobalExceptionHandler {

    @Error(global = true)
    @Produces
    public HttpResponse<Map<String, String>> handleNaoEncontrado(
            EntidadeNaoEncontradaException ex,
            HttpRequest<?> request) {
        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", ex.getMensagem()));
    }

    @Error(global = true)
    @Produces
    public HttpResponse<Map<String, String>> handleRegraDeNegocio(
            RegraDeNegocioException ex,
            HttpRequest<?> request) {
        return HttpResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("mensagem", ex.getMensagem()));
    }
}
