package com.gelco.ops.repository;

import com.gelco.ops.model.RutaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaPedidoRepository extends JpaRepository<RutaPedido, Long> {
    List<RutaPedido> findByRutaId(Long rutaId);
    List<RutaPedido> findByPedidoId(Long pedidoId);
}
