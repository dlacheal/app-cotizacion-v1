package com.cotizacion.backend.presentation.dto.response;

public record PersonaResponseDTO(
        Integer idPersona,
        String dni,
        String nombre,
        String telefono,
        String email
) {}
