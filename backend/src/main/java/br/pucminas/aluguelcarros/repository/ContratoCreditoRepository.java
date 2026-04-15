package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.ContratoCredito;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface ContratoCreditoRepository extends CrudRepository<ContratoCredito, Long> {

    Optional<ContratoCredito> findByContratoId(Long contratoId);
}

