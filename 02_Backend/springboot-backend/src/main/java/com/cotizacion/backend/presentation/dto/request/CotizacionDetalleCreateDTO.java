package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CotizacionDetalleCreateDTO(
        @NotNull(message = "El producto es obligatorio")
        Long idProducto,

        // Nuevo: Proveedor opcional (para saber origen del costo)
        Long idProveedor,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a 0")
        BigDecimal cantidad,

        // CAMBIO CRÍTICO: Ahora pedimos Costo y Margen, no precio venta directo
        @NotNull(message = "El costo unitario es obligatorio")
        @Min(value = 0, message = "El costo no puede ser negativo")
        BigDecimal costoUnitario,

        @NotNull(message = "El porcentaje de ganancia es obligatorio")
        @Min(value = 0, message = "El margen no puede ser negativo")
        BigDecimal porcentajeGanancia, // Ej: 30.00 para 30%

        // Datos visuales opcionales (si el usuario quiere sobreescribir la descripción del producto)
        String descripcionItem,
        Integer numeroLinea
) {}