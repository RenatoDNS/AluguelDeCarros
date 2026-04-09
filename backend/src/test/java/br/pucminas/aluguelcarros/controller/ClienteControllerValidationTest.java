package br.pucminas.aluguelcarros.controller;

import br.pucminas.aluguelcarros.Application;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@MicronautTest(application = Application.class)
@Property(name = "micronaut.server.port", value = "-1")
class ClienteControllerValidationTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void deveRetornar422QuandoEntidadesEmpregadorasNula() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("rg", "123456");
        payload.put("cpf", "08526348520");
        payload.put("nome", "Renato Douglas");
        payload.put("endereco", "rua 8, 117");
        payload.put("profissao", "dev backend");
        payload.put("senha", "258731");
        payload.put("entidadesEmpregadoras", null);

        HttpRequest<Map<String, Object>> request = HttpRequest.POST("/api/aluguelcarros/v1/clientes", payload)
                .contentType(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE);

        HttpClientResponseException ex = Assertions.assertThrows(HttpClientResponseException.class,
                () -> client.toBlocking().exchange(request, Argument.mapOf(String.class, Object.class)));

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());

        Map<String, Object> body = ex.getResponse()
                .getBody(Argument.mapOf(String.class, Object.class))
                .orElseGet(HashMap::new);
        Assertions.assertTrue(String.valueOf(body.get("mensagem"))
                .contains("Informe entre 1 e 3 entidades empregadoras."));
    }

    @Test
    void deveCadastrarQuandoEntidadesEmpregadorasValida() {
        Map<String, Object> entidade = new HashMap<>();
        entidade.put("nomeEmpresa", "inter");
        entidade.put("cnpj", "12345678000199");
        entidade.put("rendimento", 5000);

        Map<String, Object> payload = new HashMap<>();
        payload.put("rg", "654321");
        payload.put("cpf", "99999999999");
        payload.put("nome", "Cliente Teste");
        payload.put("endereco", "Rua A, 10");
        payload.put("profissao", "dev");
        payload.put("senha", "123456");
        payload.put("entidadesEmpregadoras", java.util.List.of(entidade));

        HttpRequest<Map<String, Object>> request = HttpRequest.POST("/api/aluguelcarros/v1/clientes", payload)
                .contentType(MediaType.APPLICATION_JSON_TYPE)
                .accept(MediaType.APPLICATION_JSON_TYPE);

        HttpResponse<Map<String, Object>> response;
        try {
            response = client.toBlocking().exchange(request, Argument.mapOf(String.class, Object.class));
        } catch (HttpClientResponseException ex) {
            Map<String, Object> erro = ex.getResponse()
                    .getBody(Argument.mapOf(String.class, Object.class))
                    .orElseGet(HashMap::new);
            Assertions.fail("Erro inesperado no cadastro valido: status=" + ex.getStatus() + ", body=" + erro);
            return;
        }

        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
    }
}

