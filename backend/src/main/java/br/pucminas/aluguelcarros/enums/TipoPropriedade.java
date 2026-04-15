package br.pucminas.aluguelcarros.enums;

public enum TipoPropriedade {
    CLIENTE,
    EMPRESA,
    BANCO;

    public static TipoPropriedade fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("tipoPropriedade");
        }
        return TipoPropriedade.valueOf(value.trim().toUpperCase());
    }
}

