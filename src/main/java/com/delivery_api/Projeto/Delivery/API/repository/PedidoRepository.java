package com.delivery_api.Projeto.Delivery.API.repository;

import com.delivery_api.Projeto.Delivery.API.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedido por número único
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    // Buscar pedidos por status (ex.: PENDENTE, ENTREGUE, CANCELADO)
    List<Pedido> findByStatus(String status);

    // Buscar pedidos por cliente (pelo id do cliente)
    List<Pedido> findByClienteId(Long clienteId);

    // Buscar pedidos por restaurante (pelo id do restaurante)
    List<Pedido> findByRestauranteId(Long restauranteId);

    // Buscar pedidos em um intervalo de datas
    List<Pedido> findByDataPedidoBetween(LocalDateTime inicio, LocalDateTime fim);

    // Combinações úteis
    List<Pedido> findByClienteIdAndStatus(Long clienteId, String status);

    List<Pedido> findByRestauranteIdAndStatus(Long restauranteId, String status);

}
