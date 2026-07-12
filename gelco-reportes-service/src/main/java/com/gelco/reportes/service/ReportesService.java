package com.gelco.reportes.service;

import com.gelco.reportes.client.CatalogoClient;
import com.gelco.reportes.client.ConsultorasClient;
import com.gelco.reportes.client.PedidosClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportesService {

    private final PedidosClient pedidosClient;
    private final ConsultorasClient consultorasClient;
    private final CatalogoClient catalogoClient;

    // HU21 — Proyección de ventas (meta 20%)
    public Map<String, Object> getProyeccionVentas() {
        List<Map<String, Object>> pedidos = pedidosClient.getAllPedidos();

        BigDecimal totalVentas = pedidos.stream()
                .map(p -> {
                    Object total = p.get("total");
                    return total != null ? new BigDecimal(total.toString()) : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal meta = totalVentas.multiply(new BigDecimal("1.20"));
        BigDecimal faltante = meta.subtract(totalVentas);

        int totalPedidos = pedidos.size();
        long pedidosEntregados = pedidos.stream()
                .filter(p -> "Entregado".equals(p.get("estado")))
                .count();

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("totalVentasActual", totalVentas);
        resultado.put("metaProyectada20pct", meta);
        resultado.put("faltanteParaMeta", faltante);
        resultado.put("totalPedidos", totalPedidos);
        resultado.put("pedidosEntregados", pedidosEntregados);
        resultado.put("porcentajeAvance", totalPedidos > 0
                ? totalVentas.divide(meta, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO);
        return resultado;
    }

    // HU22 — Estadísticas de consultora
    public Map<String, Object> getEstadisticasConsultora(Long consultoraId) {
        List<Map<String, Object>> pedidos = pedidosClient.getPedidosByConsultora(consultoraId);

        BigDecimal totalVentas = pedidos.stream()
                .map(p -> {
                    Object total = p.get("total");
                    return total != null ? new BigDecimal(total.toString()) : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalPedidos    = pedidos.size();
        long entregados      = pedidos.stream().filter(p -> "Entregado".equals(p.get("estado"))).count();
        long pendientes      = pedidos.stream().filter(p -> "Creado".equals(p.get("estado"))).count();

        // Ventas del mes actual
        int mesActual  = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();
        Map<String, Object> ventasMes = null;
        try {
            ventasMes = consultorasClient.getVentaPorMes(consultoraId, mesActual, anioActual);
        } catch (Exception ignored) {}

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("consultoraId", consultoraId);
        resultado.put("totalVentas", totalVentas);
        resultado.put("totalPedidos", totalPedidos);
        resultado.put("pedidosEntregados", entregados);
        resultado.put("pedidosPendientes", pendientes);
        resultado.put("ventasMesActual", ventasMes != null ? ventasMes.get("totalVentas") : 0);
        return resultado;
    }

    // Dashboard general
    public Map<String, Object> getDashboardGeneral() {
        List<Map<String, Object>> consultoras = consultorasClient.getAllConsultoras();
        List<Map<String, Object>> pedidos     = pedidosClient.getAllPedidos();
        List<Map<String, Object>> productos   = catalogoClient.getAllProductos();

        long productosActivos = productos.stream()
                .filter(p -> Boolean.TRUE.equals(p.get("activo")))
                .count();

        long stockBajo = productos.stream()
                .filter(p -> {
                    Object stock = p.get("stock");
                    return stock != null && Integer.parseInt(stock.toString()) <= 5;
                })
                .count();

        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("totalConsultoras", consultoras.size());
        resultado.put("totalPedidos", pedidos.size());
        resultado.put("totalProductos", productos.size());
        resultado.put("productosActivos", productosActivos);
        resultado.put("productosStockBajo", stockBajo);
        return resultado;
    }
}