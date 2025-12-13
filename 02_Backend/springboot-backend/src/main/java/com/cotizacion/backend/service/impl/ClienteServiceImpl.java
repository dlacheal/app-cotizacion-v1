package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.*;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository repository;

    @Override
    public List<Cliente> findAll() { return repository.findAll(); }

    @Override
    public Optional<Cliente> findById(Long id) { return repository.findById(id); }

    @Override
    public List<Cliente> buscarPorNombre(String termino) {
        return repository.findByNombreContainingIgnoreCase(termino);
    }

    @Override
    public Optional<Cliente> buscarPorDocumento(String rucDni) {
        return repository.findByRucDni(rucDni);
    }

    @Override
    @Transactional
    public Cliente save(Cliente cliente) { return repository.save(cliente); }

    @Override
    @Transactional
    public void delete(Long id) { repository.deleteById(id); }
}
