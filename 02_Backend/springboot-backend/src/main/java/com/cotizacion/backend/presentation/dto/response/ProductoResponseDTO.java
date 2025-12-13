package com.cotizacion.backend.presentation.dto.response;

import java.math.BigDecimal;

public record ProductoResponseDTO(
        Long idProducto,
        String nombre,
        String descripcion,

        BigDecimal precioBase,
        Boolean activo,

        // Aplanamiento de relaciones (Flattening)
        // El Mapper se encargará de extraer estos nombres (source = "categoria.nombre")
        String categoriaNombre,
        String proveedorNombre
) {}