package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.CotizacionDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotizacionDetalleRepository extends JpaRepository<CotizacionDetalle, Long> {
    // Normalmente se gestiona a través de Cotizacion (Cascade),
    // pero si necesitas borrar detalles huérfanos o análisis específicos:
    List<CotizacionDetalle> findByCotizacionIdCotizacion(Long idCotizacion);
}
