package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.CotizacionDetalle;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.CotizacionDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CotizacionDetalleServiceImpl implements CotizacionDetalleService {

    private final CotizacionDetalleRepository repository;

    @Override
    public List<CotizacionDetalle> findByCotizacion(Long idCotizacion) {
        return repository.findByCotizacionIdCotizacion(idCotizacion);
    }

    @Override
    @Transactional
    public void deleteDetalle(Long idDetalle) {
        repository.deleteById(idDetalle);
    }
}