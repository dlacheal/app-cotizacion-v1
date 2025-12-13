package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    List<Producto> findAllActivos(); // Para mostrar en ventas
    Optional<Producto> findById(Long id);
    List<Producto> findByCategoria(Long idCategoria);
    Producto save(Producto producto);
    void softDelete(Long id); // Cambiar activo a false, no borrar físico
}
