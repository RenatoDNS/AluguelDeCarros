package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.enums.ParecerResultado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "parecer",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_parecer_pedido", columnNames = "pedido_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Parecer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "banco_id")
    private Banco banco;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParecerResultado resultado;

    @Column(nullable = false, length = 1000)
    private String justificativa;

    @Column(nullable = false)
    private LocalDate dataEmissao;
}

