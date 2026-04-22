package br.pucminas.aluguelcarros.enums;

public enum PedidoTipo {
    COMPRA,
    ALUGUEL;

    public static PedidoTipo fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("tipoPedido");
        }
        return PedidoTipo.valueOf(value.trim().toUpperCase());
    }
}

