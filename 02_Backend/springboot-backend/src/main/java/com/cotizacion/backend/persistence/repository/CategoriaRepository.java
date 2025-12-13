package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Para validar no duplicados antes de guardar
    boolean existsByNombre(String nombre);
    Optional<Categoria> findByNombre(String nombre);
}
