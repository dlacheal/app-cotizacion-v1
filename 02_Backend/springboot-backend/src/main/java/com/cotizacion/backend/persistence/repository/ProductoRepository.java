package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Listar solo activos para ventas
    List<Producto> findByActivoTrue();

    // Filtrar por categoría
    List<Producto> findByCategoriaIdCategoria(Long idCategoria);

    // Búsqueda tipo "LIKE" para buscador de productos
    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
