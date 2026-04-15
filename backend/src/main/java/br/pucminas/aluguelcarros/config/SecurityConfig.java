package br.pucminas.aluguelcarros.config;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Locale;

@Singleton
public class SecurityConfig {

    private final String contextPath;

    @Inject
    public SecurityConfig(@Value("${micronaut.server.context-path:}") String contextPath) {
        this.contextPath = normalizeContextPath(contextPath);
    }

    public List<String> rotasPublicas() {
        return List.of(
                "POST /clientes",
                "POST /bancos",
                "POST /empresas",
                "POST /auth/login",
                "GET /swagger-ui"
        );
    }

    public boolean rotaPublica(String method, String path) {
        String normalizedMethod = method == null ? "" : method.trim().toUpperCase(Locale.ROOT);
        if ("OPTIONS".equals(normalizedMethod)) {
            return true;
        }
        String normalizedPath = normalizePath(path);
        return rotasPublicas().contains(normalizedMethod + " " + normalizedPath)
                || normalizedPath.startsWith("/swagger-ui");
    }

    private String normalizePath(String path) {
        String normalizedPath = path == null ? "" : path.trim();
        if (normalizedPath.isEmpty()) {
            return "/";
        }
        if (!normalizedPath.startsWith("/")) {
            normalizedPath = "/" + normalizedPath;
        }

        if (!contextPath.isEmpty() && (normalizedPath.equals(contextPath) || normalizedPath.startsWith(contextPath + "/"))) {
            normalizedPath = normalizedPath.substring(contextPath.length());
            if (normalizedPath.isEmpty()) {
                normalizedPath = "/";
            }
        }

        if (normalizedPath.length() > 1 && normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        return normalizedPath;
    }

    private String normalizeContextPath(String configuredContextPath) {
        if (configuredContextPath == null || configuredContextPath.isBlank()) {
            return "";
        }
        String normalized = configuredContextPath.trim();
        if (!normalized.startsWith("/")) {
            normalized = "/" + normalized;
        }
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }
}
