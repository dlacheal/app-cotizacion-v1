package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
    // Búsqueda simple
    Optional<Almacen> findByNombre(String nombre);
}
