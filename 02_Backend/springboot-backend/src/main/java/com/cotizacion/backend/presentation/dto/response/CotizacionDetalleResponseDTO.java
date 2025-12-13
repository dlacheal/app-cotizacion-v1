package com.cotizacion.backend.presentation.dto.response;

import java.math.BigDecimal;

public record CotizacionDetalleResponseDTO(
        Long idDetalle,
        Long idProducto,
        String productoNombre,
        String codigoArticulo,
        String unidadMedida,
        String descripcionItem,

        Integer numeroLinea,
        BigDecimal cantidad,

        // Datos financieros por línea
        BigDecimal costoUnitario,
        BigDecimal porcentajeGanancia,
        BigDecimal precioVentaUnit,    // El precio calculado
        BigDecimal subTotal,           // Cantidad * PrecioVenta
        BigDecimal impuestoMonto,      // IGV de la línea
        BigDecimal totalLinea          // SubTotal + IGV
) {}