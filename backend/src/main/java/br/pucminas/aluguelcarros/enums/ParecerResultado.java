package br.pucminas.aluguelcarros.enums;

public enum ParecerResultado {
    APROVADO,
    REJEITADO;

    public static ParecerResultado fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("resultado");
        }
        return ParecerResultado.valueOf(value.trim().toUpperCase());
    }
}

