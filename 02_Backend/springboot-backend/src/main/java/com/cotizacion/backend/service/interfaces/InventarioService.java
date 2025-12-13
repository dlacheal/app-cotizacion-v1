package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Inventario;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InventarioService {
    // Obtener stock exacto de un producto en un almacén
    Optional<Inventario> findByProductoAndAlmacen(Long idProducto, Long idAlmacen);

    // Sumar o restar stock (Lógica crítica)
    // cantidad puede ser positiva (entrada) o negativa (salida)
    Inventario ajustarStock(Long idProducto, Long idAlmacen, BigDecimal cantidad);

    // Verificar si hay suficiente stock para una venta
    boolean hayStockDisponible(Long idProducto, Long idAlmacen, BigDecimal cantidadRequerida);

    List<Inventario> listarPorProducto(Long idProducto);
    List<Inventario> listarPorAlmacen(Long idAlmacen);
    void delete(Long id);
}