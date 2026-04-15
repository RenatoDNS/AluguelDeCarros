package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Banco;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface BancoRepository extends CrudRepository<Banco, Long> {

    Optional<Banco> findByCnpj(String cnpj);
}

