package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
        name = "automovel",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_automovel_matricula", columnNames = "matricula"),
                @UniqueConstraint(name = "uk_automovel_placa", columnNames = "placa")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Automovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String matricula;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "link_imagem", nullable = false, length = 2048)
    private String linkImagem;

    @Column(name = "taxa_juros", precision = 8, scale = 4)
    private BigDecimal taxaJuros;

    @Column(nullable = false)
    private Long agentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgenteTipo agentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AutomovelStatus status;
}

