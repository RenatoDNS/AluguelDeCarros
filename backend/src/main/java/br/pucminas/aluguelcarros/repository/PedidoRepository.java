package br.pucminas.aluguelcarros.repository;

import br.pucminas.aluguelcarros.enums.AgenteTipo;
import br.pucminas.aluguelcarros.enums.PedidoStatus;
import br.pucminas.aluguelcarros.enums.PedidoTipo;
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

    List<Pedido> findByStatusAndAutomovelAgentIdAndAutomovelAgentType(PedidoStatus status,
                                                                      Long agentId,
                                                                      AgenteTipo agentType);

    List<Pedido> findByStatusAndTipoPedido(PedidoStatus status, PedidoTipo tipoPedido);

    List<Pedido> findByStatusAndTipoPedidoAndAutomovelAgentIdAndAutomovelAgentType(PedidoStatus status,
                                                                                    PedidoTipo tipoPedido,
                                                                                    Long agentId,
                                                                                    AgenteTipo agentType);
}

