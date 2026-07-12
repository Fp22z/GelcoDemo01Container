package com.gelco.devoluciones.repository;

import com.gelco.devoluciones.model.Devolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Integer> {

    @Query("SELECT COALESCE(SUM(d.cantidad), 0) FROM Devolucion d WHERE d.detallePedidoId = :detallePedidoId")
    Integer sumCantidadDevueltaByDetalleId(@Param("detallePedidoId") Long detallePedidoId);

    @Query("SELECT d FROM Devolucion d JOIN FETCH d.recepcionista WHERE d.id = :id")
    Optional<Devolucion> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT d FROM Devolucion d JOIN FETCH d.recepcionista " +
            "WHERE d.detallePedidoId = :detallePedidoId ORDER BY d.fechaSolicitud DESC")
    List<Devolucion> findByDetallePedidoId(@Param("detallePedidoId") Long detallePedidoId);

    @Query("SELECT d FROM Devolucion d JOIN FETCH d.recepcionista ORDER BY d.fechaSolicitud DESC")
    List<Devolucion> findAllWithRelations();
}