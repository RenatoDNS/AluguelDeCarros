package br.pucminas.aluguelcarros.enums;

public enum AgenteTipo {
    BANCO,
    EMPRESA;

    public static AgenteTipo fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("agenteTipo");
        }
        return AgenteTipo.valueOf(value.trim().toUpperCase());
    }
}

