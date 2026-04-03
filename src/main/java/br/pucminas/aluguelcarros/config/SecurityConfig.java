package br.pucminas.aluguelcarros.config;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class SecurityConfig {

    public List<String> rotasPublicas() {
        return List.of(
                "POST /clientes",
                "POST /auth/login",
                "GET /swagger-ui",
                "GET /swagger-ui/",
                "GET /swagger-ui/index.html",
                "GET /swagger.yml"
        );
    }

    public boolean rotaPublica(String method, String path) {
        String normalizedPath = path == null ? "" : path;
        if (rotasPublicas().contains(method + " " + normalizedPath)) {
            return true;
        }
        return normalizedPath.contains("/swagger-ui")
                || normalizedPath.endsWith("/swagger.yml");
    }
}
