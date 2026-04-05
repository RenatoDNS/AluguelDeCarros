package br.pucminas.aluguelcarros.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/swagger-ui")
public class SwaggerController {

    private static final String SWAGGER_UI_URL =
            "/api/aluguelcarros/v1/swagger-ui/index.html?url=/api/aluguelcarros/v1/swagger.yml";

    @Get
    public HttpResponse<?> index() {
        return HttpResponse.redirect(java.net.URI.create(SWAGGER_UI_URL));
    }
}
