package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Buscar cliente por RUC o DNI
    Optional<Cliente> findByRucDni(String rucDni);
    // Búsqueda parcial para autocompletados en el frontend (IgnoreCase)
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
}
