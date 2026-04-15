package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.AutomovelStatus;
import br.pucminas.aluguelcarros.model.Automovel;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutomovelRepository extends CrudRepository<Automovel, Long> {

    Optional<Automovel> findByMatricula(String matricula);

    Optional<Automovel> findByPlaca(String placa);

    List<Automovel> findByStatus(AutomovelStatus status);

    List<Automovel> findByAgentIdAndAgentType(Long agentId, AgenteTipo agentType);
}

