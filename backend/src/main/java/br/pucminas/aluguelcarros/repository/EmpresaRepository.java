package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.model.Empresa;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends CrudRepository<Empresa, Long> {

    Optional<Empresa> findByCnpj(String cnpj);
}

