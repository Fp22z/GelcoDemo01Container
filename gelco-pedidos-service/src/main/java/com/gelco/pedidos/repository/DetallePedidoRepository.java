package com.gelco.pedidos.repository;

import com.gelco.pedidos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);

    @Query("SELECT d.productoId, SUM(d.cantidad) as totalCantidad " +
            "FROM DetallePedido d " +
            "WHERE d.pedido.fecha >= :fechaDesde " +
            "AND d.pedido.estado = 'Entregado' " +
            "GROUP BY d.productoId " +
            "ORDER BY totalCantidad DESC")
    List<Object[]> findVentasAgrupadasPorProducto(@Param("fechaDesde") LocalDateTime fechaDesde);

    @Query("SELECT d.productoId, SUM(d.cantidad) as totalCantidad " +
            "FROM DetallePedido d " +
            "WHERE d.pedido.estado = 'Entregado' " +
            "GROUP BY d.productoId " +
            "ORDER BY totalCantidad DESC")
    List<Object[]> findVentasAgrupadasPorProductoTodos();
}