package br.pucminas.aluguelcarros.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SecurityConfigTest {

    @Test
    void devePermitirRotaPublicaSemPrefixo() {
        SecurityConfig securityConfig = new SecurityConfig("/api/aluguelcarros/v1");

        Assertions.assertTrue(securityConfig.rotaPublica("POST", "/clientes"));
    }

    @Test
    void devePermitirRotaPublicaComContextPath() {
        SecurityConfig securityConfig = new SecurityConfig("/api/aluguelcarros/v1");

        Assertions.assertTrue(securityConfig.rotaPublica("POST", "/api/aluguelcarros/v1/clientes"));
    }

    @Test
    void devePermitirSwaggerComSubrota() {
        SecurityConfig securityConfig = new SecurityConfig("/api/aluguelcarros/v1");

        Assertions.assertTrue(securityConfig.rotaPublica("GET", "/api/aluguelcarros/v1/swagger-ui/index.html"));
    }

    @Test
    void naoDevePermitirRotaProtegidaSemToken() {
        SecurityConfig securityConfig = new SecurityConfig("/api/aluguelcarros/v1");

        Assertions.assertFalse(securityConfig.rotaPublica("GET", "/api/aluguelcarros/v1/clientes/1"));
    }
}

