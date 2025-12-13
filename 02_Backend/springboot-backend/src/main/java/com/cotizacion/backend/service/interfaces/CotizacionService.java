package com.cotizacion.backend.service.interfaces;

import com.cotizacion.backend.persistence.entity.Cotizacion;
import com.cotizacion.backend.persistence.entity.CotizacionDetalle;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface CotizacionService {
    // Listados
    List<Cotizacion> findAll();
    List<Cotizacion> findByCliente(Long idCliente);
    List<Cotizacion> findByFechaRango(LocalDateTime inicio, LocalDateTime fin);

    Optional<Cotizacion> findById(Long id);

    // Transaccional
    // Este método recibirá la cabecera y los detalles, calculará totales e impuestos
    Cotizacion crearCotizacion(Cotizacion cotizacion, List<CotizacionDetalle> detalles);

    // Recálculo si se edita una cotización existente
    Cotizacion actualizarCotizacion(Long idCotizacion, Cotizacion cotizacion, List<CotizacionDetalle> nuevosDetalles);

    // Cambios de estado (Ej: de "BORRADOR" a "APROBADA")
    void cambiarEstado(Long idCotizacion, String nombreNuevoEstado);

    // Generar PDF (Opcional, futuro)
    // byte[] generarReportePdf(Long idCotizacion);
}
