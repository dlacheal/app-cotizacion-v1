package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductoRequestDTO(
        @NotBlank(message = "El nombre del producto es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres")
        String nombre,

        @Size(max = 2000, message = "La descripción es demasiado larga") // Asumiendo TEXT o límite razonable
        String descripcion,

        @NotNull(message = "La categoría es obligatoria")
        Long idCategoria,

        @NotNull(message = "El proveedor es obligatorio")
        Long idProveedor,

        @NotNull(message = "El precio base es obligatorio")
        @PositiveOrZero(message = "El precio base no puede ser negativo")
        @Digits(integer = 10, fraction = 2, message = "El precio excede el formato permitido (12,2)")
        BigDecimal precioBase

        // Nota: No incluimos 'activo' aquí.
        // Por defecto al crear es TRUE (ver Mapper).
        // Para desactivar/activar, se suele usar un endpoint específico PATCH (softDelete).
) {}
