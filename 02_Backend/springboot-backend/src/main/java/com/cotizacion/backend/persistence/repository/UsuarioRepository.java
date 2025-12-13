package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Método principal para el Login
    Optional<Usuario> findByUsername(String username);

    // Método para validar existencia al registrar
    boolean existsByUsername(String username);
}