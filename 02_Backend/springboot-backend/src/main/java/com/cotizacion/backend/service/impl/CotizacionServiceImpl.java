package com.cotizacion.backend.service.impl;

import com.cotizacion.backend.persistence.entity.Cotizacion;
import com.cotizacion.backend.persistence.entity.CotizacionDetalle;
import com.cotizacion.backend.persistence.entity.EstadoCotizacion;
import com.cotizacion.backend.persistence.repository.*;
import com.cotizacion.backend.service.interfaces.CotizacionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CotizacionServiceImpl implements CotizacionService {

    private final CotizacionRepository repository;
    private final EstadoCotizacionRepository estadoRepository;
    private final ClienteRepository clienteRepository;
    // private final ProductoRepository productoRepository; // Útil si necesitas
    // validar existencias

    @Override
    public List<Cotizacion> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Cotizacion> findByCliente(Long idCliente) {
        return repository.findByClienteIdCliente(idCliente);
    }

    @Override
    public List<Cotizacion> findByFechaRango(LocalDateTime inicio, LocalDateTime fin) {
        return repository.findByFechaBetween(inicio, fin);
    }

    @Override
    public Optional<Cotizacion> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Cotizacion crearCotizacion(Cotizacion cotizacion, List<CotizacionDetalle> detalles) {
        // 1. Validar Cliente
        if (!clienteRepository.existsById(cotizacion.getCliente().getIdCliente())) {
            throw new EntityNotFoundException("Cliente no encontrado");
        }

        // 2. Asignar estado inicial si es nulo
        if (cotizacion.getEstado() == null) {
            EstadoCotizacion estadoInicial = estadoRepository.findByNombre("PENDIENTE")
                    .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no configurado en BD"));
            cotizacion.setEstado(estadoInicial);
        }

        // 3. Vincular detalles a la cabecera
        // Limpiamos la lista interna por seguridad y agregamos los nuevos para activar
        // el helper
        cotizacion.getDetalles().clear();
        detalles.forEach(cotizacion::addDetalle);

        // 4. Calcular todos los montos (Lógica de Negocio Financiera)
        recalcularMontos(cotizacion);

        // 5. Configurar fecha si no viene
        if (cotizacion.getFecha() == null) {
            cotizacion.setFecha(LocalDateTime.now());
        }

        return repository.save(cotizacion);
    }

    @Override
    @Transactional
    public Cotizacion actualizarCotizacion(Long idCotizacion, Cotizacion datosNuevos,
            List<CotizacionDetalle> nuevosDetalles) {
        Cotizacion cotizacionExistente = findById(idCotizacion)
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada"));

        // 1. Actualizar datos de cabecera simples
        cotizacionExistente.setObservaciones(datosNuevos.getObservaciones());
        cotizacionExistente.setFormaPago(datosNuevos.getFormaPago());
        cotizacionExistente.setValidezDias(datosNuevos.getValidezDias());
        cotizacionExistente.setMoneda(datosNuevos.getMoneda());

        // 2. Actualizar Cliente si cambió
        if (!cotizacionExistente.getCliente().equals(datosNuevos.getCliente())) {
            if (!clienteRepository.existsById(datosNuevos.getCliente().getIdCliente())) {
                throw new EntityNotFoundException("Nuevo cliente no encontrado");
            }
            cotizacionExistente.setCliente(datosNuevos.getCliente());
        }

        // 3. Reemplazar detalles (Estrategia simple: borrar y reinsertar)
        cotizacionExistente.getDetalles().clear();
        nuevosDetalles.forEach(cotizacionExistente::addDetalle);

        // 4. Recalcular montos con la nueva lista
        recalcularMontos(cotizacionExistente);

        return repository.save(cotizacionExistente);
    }

    @Override
    @Transactional
    public void cambiarEstado(Long idCotizacion, String nombreNuevoEstado) {
        Cotizacion cotizacion = findById(idCotizacion)
                .orElseThrow(() -> new EntityNotFoundException("Cotización no encontrada"));

        EstadoCotizacion nuevoEstado = estadoRepository.findByNombre(nombreNuevoEstado)
                .orElseThrow(() -> new EntityNotFoundException("Estado no encontrado: " + nombreNuevoEstado));

        cotizacion.setEstado(nuevoEstado);
        repository.save(cotizacion);
    }

    /**
     * Lógica central de cálculo financiero.
     * Calcula línea por línea basándose en Costo, Margen y Cantidad,
     * y luego sumariza en la Cabecera.
     */
    private void recalcularMontos(Cotizacion cotizacion) {
        BigDecimal totalInversion = BigDecimal.ZERO;
        BigDecimal totalUtilidadBruta = BigDecimal.ZERO;
        BigDecimal totalSubTotal = BigDecimal.ZERO;
        BigDecimal totalIgv = BigDecimal.ZERO;
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (CotizacionDetalle d : cotizacion.getDetalles()) {
            // --- CÁLCULOS A NIVEL DE DETALLE ---

            // 1. Costo Total = Cantidad * Costo Unitario
            BigDecimal costoTotal = d.getCantidad().multiply(d.getCostoUnitario());
            d.setCostoTotal(costoTotal);

            // 2. Inversión con IGV (Asumimos que al proveedor le pagamos IGV)
            // Inversion = Costo Total * 1.18
            BigDecimal factorIgv = new BigDecimal("1.18");
            d.setInversionTotalIncIgv(costoTotal.multiply(factorIgv).setScale(2, RoundingMode.HALF_UP));

            // 3. Precio Venta Unitario (Basado en Margen)
            // Precio Venta = Costo Unitario * (1 + (%Ganancia / 100))
            BigDecimal porcentajeGananciaDecimal = d.getPorcentajeGanancia().divide(new BigDecimal("100"), 4,
                    RoundingMode.HALF_UP);
            BigDecimal factorGanancia = BigDecimal.ONE.add(porcentajeGananciaDecimal);
            BigDecimal precioVentaUnit = d.getCostoUnitario().multiply(factorGanancia).setScale(4,
                    RoundingMode.HALF_UP);
            d.setPrecioVentaUnit(precioVentaUnit);

            // 4. Utilidad Unitaria
            d.setUtilidadUnitaria(precioVentaUnit.subtract(d.getCostoUnitario()));

            // 5. SubTotal Venta = Cantidad * Precio Venta Unit
            BigDecimal subTotalLinea = d.getCantidad().multiply(precioVentaUnit).setScale(2, RoundingMode.HALF_UP);
            d.setSubTotal(subTotalLinea);

            // 6. Utilidad Bruta Linea = SubTotal - Costo Total
            d.setUtilidadBruta(subTotalLinea.subtract(costoTotal));

            // 7. Impuesto (IGV) hacia el Cliente
            BigDecimal impuestoPorcentDecimal = d.getImpuestoPorcent().divide(new BigDecimal("100"), 4,
                    RoundingMode.HALF_UP);
            BigDecimal montoImpuesto = subTotalLinea.multiply(impuestoPorcentDecimal).setScale(2, RoundingMode.HALF_UP);
            d.setImpuestoMonto(montoImpuesto);

            // 8. Total Linea
            d.setTotalLinea(subTotalLinea.add(montoImpuesto));

            // --- ACUMULACIÓN EN VARIABLES LOCALES ---
            totalInversion = totalInversion.add(d.getInversionTotalIncIgv());
            totalUtilidadBruta = totalUtilidadBruta.add(d.getUtilidadBruta());
            totalSubTotal = totalSubTotal.add(subTotalLinea);
            totalIgv = totalIgv.add(montoImpuesto);
            totalGeneral = totalGeneral.add(d.getTotalLinea());
        }

        // --- SETTEO FINAL A CABECERA ---
        cotizacion.setInversionTotal(totalInversion);
        cotizacion.setUtilidadBrutaTotal(totalUtilidadBruta);
        cotizacion.setSubTotal(totalSubTotal);
        cotizacion.setMontoIgv(totalIgv);
        cotizacion.setTotalGeneral(totalGeneral);
    }
}