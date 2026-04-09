package br.pucminas.aluguelcarros.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import java.util.Map;

@Produces
@Singleton
public class EntidadeNaoEncontradaExceptionHandler implements ExceptionHandler<EntidadeNaoEncontradaException, HttpResponse<Map<String, String>>> {

    @Override
    public HttpResponse<Map<String, String>> handle(HttpRequest request, EntidadeNaoEncontradaException exception) {
        return HttpResponse.status(HttpStatus.NOT_FOUND)
                .body(Map.of("mensagem", exception.getMensagem()));
    }
}

