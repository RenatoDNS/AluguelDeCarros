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
public class RegraDeNegocioExceptionHandler implements ExceptionHandler<RegraDeNegocioException, HttpResponse<Map<String, String>>> {

    @Override
    public HttpResponse<Map<String, String>> handle(HttpRequest request, RegraDeNegocioException exception) {
        return HttpResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("mensagem", exception.getMensagem()));
    }
}

