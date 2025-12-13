package com.cotizacion.backend.presentation.dto.response;

public record AlmacenResponseDTO(
        Long idAlmacen,
        String nombre,
        String direccion
) {}
