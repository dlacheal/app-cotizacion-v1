package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Authority;

import java.util.List;
import java.util.Optional;

public interface AuthorityService {
    List<Authority> findRolesByUsuario(Integer idUsuario);
    Authority addRoleToUsuario(Integer idUsuario, String roleName);
    void removeRoleFromUsuario(Integer idUsuario, String roleName);
}
