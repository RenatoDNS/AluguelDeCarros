package br.pucminas.aluguelcarros.enums;

public enum PedidoStatus {
    EM_ANALISE,
    APROVADO,
    REJEITADO,
    CANCELADO;

    public static PedidoStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("status");
        }
        return PedidoStatus.valueOf(value.trim().toUpperCase());
    }
}

