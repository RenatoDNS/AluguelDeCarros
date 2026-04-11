package br.pucminas.aluguelcarros.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
* Por algum motivo notações como @Data, @Getter, @Setter, @NoArgsConstructor do Lombok não estão funcionando, então os métodos foram escritos manualmente.
* Vou tentar resolver isso depois.
* */


@Entity
@Table(
        name = "cliente",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cliente_cpf", columnNames = "cpf"),
                @UniqueConstraint(name = "uk_cliente_rg", columnNames = "rg")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Cliente extends Usuario {

    @Column(nullable = false, unique = true)
    private String rg;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String profissao;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EntidadeEmpregadora> entidadesEmpregadoras = new ArrayList<>();
}
