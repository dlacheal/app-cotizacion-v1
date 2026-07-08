package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Page<Usuario> findAll(Pageable pageable);
    Optional<Usuario> findById(Integer id);
    Optional<Usuario> findByUsername(String username); // Esencial para Login
    Usuario registrarUsuario(Usuario usuario); // Lógica de encriptación password aquí
    Usuario save(Usuario usuario);
    void deshabilitarUsuario(Integer id); // Soft delete (Enable = false)
    void delete(Integer id);
}
