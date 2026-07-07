package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);

    @Query("SELECT r FROM Usuario u JOIN u.roles r WHERE u.idUsuario = :idUsuario")
    List<Rol> findRolesByUsuarioId(@Param("idUsuario") Integer idUsuario);
}
