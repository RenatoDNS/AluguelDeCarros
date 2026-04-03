package br.pucminas.aluguelcarros.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Controller
public class OpenApiController {

    @Get("/swagger.yml")
    public HttpResponse<String> spec() throws IOException {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("swagger.yml")) {
            if (inputStream == null) {
                return HttpResponse.status(HttpStatus.NOT_FOUND);
            }
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return HttpResponse.ok(content)
                    .header(HttpHeaders.CONTENT_TYPE, "application/yaml");
        }
    }
}
