package br.pucminas.aluguelcarros.model;

import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
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
        name = "pedido",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pedido_numero_protocolo", columnNames = "numero_protocolo")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_protocolo", nullable = false, unique = true)
    private String numeroProtocolo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "automovel_id")
    private Automovel automovel;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pedido", nullable = false)
    private PedidoTipo tipoPedido;

    @Column
    private LocalDate dataInicio;

    @Column
    private LocalDate dataFim;

    @Column(name = "qntd_parcelas")
    private Integer qntdParcelas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PedidoStatus status;

    @Column(length = 1000)
    private String justificativa;
}

