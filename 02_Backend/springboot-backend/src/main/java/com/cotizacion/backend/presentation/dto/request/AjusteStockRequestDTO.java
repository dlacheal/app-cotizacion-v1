package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AjusteStockRequestDTO(
        @NotNull(message = "El ID del producto es obligatorio")
        Long idProducto,

        @NotNull(message = "El ID del almacén es obligatorio")
        Long idAlmacen,

        @NotNull(message = "La cantidad a ajustar es obligatoria")
        // Nota: No usamos @Positive para permitir valores negativos (salidas de stock)
        // Sin embargo, validamos que no sea cero, ya que un ajuste de 0 es inútil.
        // Podrías crear una validación personalizada @NotZero si fuera necesario.
        BigDecimal cantidad
) {
    // Constructor compacto para validación extra (Feature de Java Records)
    public AjusteStockRequestDTO {
        if (cantidad != null && cantidad.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("La cantidad de ajuste no puede ser cero");
        }
    }
}
