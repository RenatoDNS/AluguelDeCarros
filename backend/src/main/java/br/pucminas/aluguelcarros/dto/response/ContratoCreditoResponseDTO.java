package br.pucminas.aluguelcarros.dto.response;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Serdeable
public record ContratoCreditoResponseDTO(
        Long id,
        BancoResumoDTO banco,
        ClienteResumoDTO cliente,
        VeiculoResumoDTO veiculo,
        Integer quantidadeParcelas,
        BigDecimal valorParcela,
        BigDecimal valorTotal,
        LocalDate dataAssinaturaBanco,
        LocalDate dataAssinaturaCliente,
        boolean bancoAssinou,
        boolean clienteAssinou
) {
}

