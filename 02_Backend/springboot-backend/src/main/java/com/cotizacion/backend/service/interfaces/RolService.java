package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Rol;

import java.util.List;

public interface RolService {
    List<Rol> findRolesByUsuario(Integer idUsuario);
    Rol addRoleToUsuario(Integer idUsuario, String roleName);
    void removeRoleFromUsuario(Integer idUsuario, String roleName);
}
