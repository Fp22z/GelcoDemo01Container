package com.gelco.ventas.repository;

import com.gelco.ventas.model.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    List<OrdenCompra> findByPedidoConsultoraUsuarioId(Long usuarioId);
    boolean existsByPedidoId(Long pedidoId);
}