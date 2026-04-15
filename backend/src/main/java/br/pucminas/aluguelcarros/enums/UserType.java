package br.pucminas.aluguelcarros.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum UserType {
    CLIENTE("cliente"),
    EMPRESA("empresa"),
    BANCO("banco");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    public static UserType fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("userType inválido.");
        }
        for (UserType type : values()) {
            if (type.value.equalsIgnoreCase(value.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("userType inválido.");
    }
}


