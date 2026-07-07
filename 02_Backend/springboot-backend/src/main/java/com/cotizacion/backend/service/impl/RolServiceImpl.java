package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.Rol;
import com.cotizacion.backend.persistence.entity.Usuario;
import com.cotizacion.backend.persistence.repository.RolRepository;
import com.cotizacion.backend.persistence.repository.UsuarioRepository;
import com.cotizacion.backend.service.interfaces.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolServiceImpl implements RolService {

    private final RolRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Rol> findRolesByUsuario(Integer idUsuario) {
        return repository.findRolesByUsuarioId(idUsuario);
    }

    @Override
    @Transactional
    public Rol addRoleToUsuario(Integer idUsuario, String roleName) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = repository.findByNombre(roleName)
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre(roleName);
                    return repository.save(nuevoRol);
                });

        if (!usuario.getRoles().contains(rol)) {
            usuario.getRoles().add(rol);
            usuarioRepository.save(usuario);
        }

        return rol;
    }

    @Override
    @Transactional
    public void removeRoleFromUsuario(Integer idUsuario, String roleName) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.getRoles().removeIf(rol -> rol.getNombre().equals(roleName));
        usuarioRepository.save(usuario);
    }
}
