package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> findAll();
    Optional<Proveedor> findById(Long id);
    List<Proveedor> buscarPorNombre(String termino);
    Optional<Proveedor> buscarPorRuc(String ruc);
    Proveedor save(Proveedor proveedor);
    void delete(Long id);
}