package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "banco",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_banco_cnpj", columnNames = "cnpj")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Banco extends Agente {

    private String codigoBancario;
}
