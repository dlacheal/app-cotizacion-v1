package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.CotizacionDetalle;

import java.util.List;

public interface CotizacionDetalleService {
    // NOTA: Generalmente el detalle se gestiona A TRAVÉS de la Cotizacion (Aggregate Root).
    // Pero si necesitas operaciones aisladas:
    List<CotizacionDetalle> findByCotizacion(Long idCotizacion);

    // Borrar un item específico de una cotización
    void deleteDetalle(Long idDetalle);
}
