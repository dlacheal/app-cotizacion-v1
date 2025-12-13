package com.cotizacion.backend.persistence.repository;

import com.cotizacion.backend.persistence.entity.Cotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal; // Importante importar esto
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

    // Filtrar cotizaciones de un cliente
    List<Cotizacion> findByClienteIdCliente(Long idCliente);

    // Filtrar por rango de fechas (Reportes)
    List<Cotizacion> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    // Filtrar por estado (Ej: "PENDIENTE", "APROBADA")
    List<Cotizacion> findByEstadoNombre(String nombreEstado);

    // CORREGIDO:
    // Antes: ... WHERE c.totalNeto > :monto
    // Ahora: ... WHERE c.totalGeneral > :monto (Usamos el nuevo campo total)
    @Query("SELECT c FROM Cotizacion c WHERE c.totalGeneral > :monto")
    List<Cotizacion> findCotizacionesDeAltoValor(@Param("monto") BigDecimal monto);
}