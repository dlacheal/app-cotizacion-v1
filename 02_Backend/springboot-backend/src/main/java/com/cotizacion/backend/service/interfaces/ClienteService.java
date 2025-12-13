package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    // Búsqueda "fuzzy" para autocompletados en UI
    List<Cliente> buscarPorNombre(String termino);
    Optional<Cliente> buscarPorDocumento(String rucDni);
    Cliente save(Cliente cliente);
    void delete(Long id);
}
