package br.pucminas.aluguelcarros.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Agente extends Usuario {

    private String razaoSocial;

    private String cnpj;
}
