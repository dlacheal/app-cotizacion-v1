package com.cotizacion.backend.presentation.dto.response;

import java.math.BigDecimal;

public record InventarioResponseDTO(
        Long idInventario,
        String productoNombre,
        String almacenNombre,
        BigDecimal stockActual
) {}
