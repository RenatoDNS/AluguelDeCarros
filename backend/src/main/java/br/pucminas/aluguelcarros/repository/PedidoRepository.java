package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.model.Pedido;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends CrudRepository<Pedido, Long> {

    Optional<Pedido> findByNumeroProtocolo(String numeroProtocolo);

    List<Pedido> findByStatus(PedidoStatus status);

    List<Pedido> findByClienteId(Long clienteId);
}

