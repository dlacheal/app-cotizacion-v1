package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Buscar stock de un producto específico en un almacén específico
    // Spring Data interpreta los guiones bajos _ como navegación de objetos
    Optional<Inventario> findByProducto_IdProductoAndAlmacen_IdAlmacen(Long idProducto, Long idAlmacen);

    // Ver stock total de un producto en todos los almacenes
    List<Inventario> findByProducto_IdProducto(Long idProducto);

    List<Inventario> findByAlmacen_IdAlmacen(Long idAlmacen);
}
