package com.gelco.ventas.repository;

import com.gelco.ventas.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByPedidoConsultoraUsuarioId(Long usuarioId);
    boolean existsByPedidoId(Long pedidoId);
}