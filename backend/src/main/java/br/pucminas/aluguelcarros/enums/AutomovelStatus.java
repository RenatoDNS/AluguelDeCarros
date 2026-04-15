package br.pucminas.aluguelcarros.enums;

public enum AutomovelStatus {
    DISPONIVEL,
    ALUGADO,
    VINCULADO;

    public static AutomovelStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("status");
        }
        return AutomovelStatus.valueOf(value.trim().toUpperCase());
    }
}


