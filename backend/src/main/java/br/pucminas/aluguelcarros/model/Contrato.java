package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contrato")
@Getter
@Setter
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id")
    private Automovel veiculo;

    @Column(name = "data_inicio_aluguel", nullable = false)
    private LocalDate dataInicioAluguel;

    @Column(name = "data_fim_aluguel", nullable = false)
    private LocalDate dataFimAluguel;

    @Column(name = "valor_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valor_diaria", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorDiaria;

    @Column(name = "data_assinatura_empresa")
    private LocalDate dataAssinaturaEmpresa;

    @Column(name = "data_assinatura_cliente")
    private LocalDate dataAssinaturaCliente;

    @Column(name = "empresa_assinou", nullable = false)
    private boolean empresaAssinou;

    @Column(name = "cliente_assinou", nullable = false)
    private boolean clienteAssinou;
}

