package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByUsername(String username); // Esencial para Login
    Usuario registrarUsuario(Usuario usuario); // Lógica de encriptación password aquí
    void deshabilitarUsuario(Integer id); // Soft delete (Enable = false)
}
