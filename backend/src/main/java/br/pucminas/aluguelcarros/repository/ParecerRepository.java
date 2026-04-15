package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Parecer;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface ParecerRepository extends CrudRepository<Parecer, Long> {

    Optional<Parecer> findByPedidoId(Long pedidoId);
}

