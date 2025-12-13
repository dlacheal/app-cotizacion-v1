package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.Authority;
import com.cotizacion.backend.persistence.entity.Usuario;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorityServiceImpl implements AuthorityService {
    private final AuthorityRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Authority> findRolesByUsuario(Integer idUsuario) {
        return repository.findByUsuarioIdUsuario(idUsuario);
    }

    @Override
    @Transactional
    public Authority addRoleToUsuario(Integer idUsuario, String roleName) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Authority authority = new Authority();
        authority.setTipo(roleName);
        authority.setUsuario(usuario);
        return repository.save(authority);
    }

    @Override
    @Transactional
    public void removeRoleFromUsuario(Integer idUsuario, String roleName) {
        // Implementación simplificada
        List<Authority> roles = repository.findByUsuarioIdUsuario(idUsuario);
        roles.stream()
                .filter(a -> a.getTipo().equals(roleName))
                .findFirst()
                .ifPresent(repository::delete);
    }
}
