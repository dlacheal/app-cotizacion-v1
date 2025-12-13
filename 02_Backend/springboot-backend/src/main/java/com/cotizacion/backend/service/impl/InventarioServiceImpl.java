package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.Inventario;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventarioServiceImpl implements InventarioService {
    private final InventarioRepository repository;
    private final ProductoRepository productoRepository;
    private final AlmacenRepository almacenRepository;

    @Override
    public Optional<Inventario> findByProductoAndAlmacen(Long idProducto, Long idAlmacen) {
        return repository.findByProducto_IdProductoAndAlmacen_IdAlmacen(idProducto, idAlmacen);
    }

    @Override
    @Transactional
    public Inventario ajustarStock(Long idProducto, Long idAlmacen, BigDecimal cantidad) {
        Inventario inventario = repository.findByProducto_IdProductoAndAlmacen_IdAlmacen(idProducto, idAlmacen)
                .orElseGet(() -> {
                    // Si no existe inventario, lo creamos
                    Inventario nuevo = new Inventario();
                    nuevo.setProducto(productoRepository.findById(idProducto).orElseThrow());
                    nuevo.setAlmacen(almacenRepository.findById(idAlmacen).orElseThrow());
                    nuevo.setStockActual(BigDecimal.ZERO);
                    return nuevo;
                });

        // Sumar (o restar si es negativo)
        BigDecimal nuevoStock = inventario.getStockActual().add(cantidad);

        if (nuevoStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Stock insuficiente para realizar esta operación");
        }

        inventario.setStockActual(nuevoStock);
        return repository.save(inventario);
    }

    @Override
    public boolean hayStockDisponible(Long idProducto, Long idAlmacen, BigDecimal cantidadRequerida) {
        return repository.findByProducto_IdProductoAndAlmacen_IdAlmacen(idProducto, idAlmacen)
                .map(inv -> inv.getStockActual().compareTo(cantidadRequerida) >= 0)
                .orElse(false);
    }

    @Override
    public List<Inventario> listarPorProducto(Long idProducto) {
        return repository.findByProducto_IdProducto(idProducto);
    }

    @Override
    public List<Inventario> listarPorAlmacen(Long idAlmacen) {
        // Necesitas agregar findByAlmacen_IdAlmacen en InventarioRepository
        return repository.findByAlmacen_IdAlmacen(idAlmacen);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
