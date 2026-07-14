package com.gelco.pedidos.repository;

import com.gelco.pedidos.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    @Query("SELECT f FROM Factura f JOIN f.pedido p WHERE p.consultoraId = :consultoraId")
    List<Factura> findByPedidoConsultoraId(@Param("consultoraId") Long consultoraId);

    boolean existsByPedidoId(Long pedidoId);
}