package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Serdeable
public record ContratoResponseDTO(
        Long id,
        EmpresaResumoDTO empresa,
        ClienteResumoDTO cliente,
        VeiculoResumoDTO veiculo,
        LocalDate dataInicioAluguel,
        LocalDate dataFimAluguel,
        BigDecimal valorTotal,
        BigDecimal valorDiaria,
        LocalDate dataAssinaturaEmpresa,
        LocalDate dataAssinaturaCliente,
        boolean empresaAssinou,
        boolean clienteAssinou
) {
}

