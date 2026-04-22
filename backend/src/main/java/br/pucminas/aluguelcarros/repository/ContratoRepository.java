package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Contrato;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ContratoRepository extends CrudRepository<Contrato, Long> {

    Optional<Contrato> findByClienteIdAndVeiculoIdAndDataInicioAluguelAndDataFimAluguel(Long clienteId,
                                                                                          Long veiculoId,
                                                                                          LocalDate dataInicioAluguel,
                                                                                          LocalDate dataFimAluguel);
}
