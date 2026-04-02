package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "banco")
@Getter
@Setter
@NoArgsConstructor
public class Banco extends Agente {

    private String codigoBancario;
}
