package com.cotizacion.backend.presentation.dto.response;

public record ProveedorResponseDTO(
        Long idProveedor,
        String nombre,
        String ruc,
        String telefono,
        String email,
        String direccion
) {}
