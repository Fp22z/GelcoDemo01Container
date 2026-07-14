package com.gelco.pedidos.repository;

import com.gelco.pedidos.model.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    @Query("SELECT oc FROM OrdenCompra oc JOIN oc.pedido p WHERE p.consultoraId = :consultoraId")
    List<OrdenCompra> findByPedidoConsultoraId(@Param("consultoraId") Long consultoraId);

    boolean existsByPedidoId(Long pedidoId);
}