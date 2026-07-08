package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.Usuario;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> findAll() { return repository.findAll(); }

    @Override
    public Page<Usuario> findAll(Pageable pageable) { return repository.findAll(pageable); }

    @Override
    public Optional<Usuario> findById(Integer id) { return repository.findById(id); }

    @Override
    public Optional<Usuario> findByUsername(String username) { return repository.findByUsername(username); }

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if(repository.existsByUsername(usuario.getUsername())) throw new RuntimeException("Usuario existe");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void deshabilitarUsuario(Integer id) {
        repository.findById(id).ifPresent(user -> {
            user.setEnable(false);
            repository.save(user);
        });
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
