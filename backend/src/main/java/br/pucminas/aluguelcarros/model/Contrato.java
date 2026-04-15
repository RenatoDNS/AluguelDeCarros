package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.enums.TipoPropriedade;
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
        name = "contrato",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_contrato_pedido", columnNames = "pedido_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_propriedade", nullable = false)
    private TipoPropriedade tipoPropriedade;

    @Column(name = "data_assinatura", nullable = false)
    private LocalDate dataAssinatura;
}

