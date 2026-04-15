package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "empresa",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_empresa_cnpj", columnNames = "cnpj")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Empresa extends Agente {

    private String ramoDeAtividade;
}
