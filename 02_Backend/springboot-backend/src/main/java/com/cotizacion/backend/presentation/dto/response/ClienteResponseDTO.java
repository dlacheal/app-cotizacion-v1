package com.cotizacion.backend.presentation.dto.response;

public record ClienteResponseDTO(
        Long idCliente,
        String nombre,
        String rucDni,
        String telefono,
        String email,
        String direccion
) {}
