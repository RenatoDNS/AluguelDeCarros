package br.pucminas.aluguelcarros.config;

import jakarta.inject.Singleton;

import java.util.List;

@Singleton
public class SecurityConfig {

    public List<String> rotasPublicas() {
        return List.of(
                "POST /clientes",
                "POST /auth/login"
        );
    }
}
