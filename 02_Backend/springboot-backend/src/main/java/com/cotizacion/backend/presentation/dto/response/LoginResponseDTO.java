package com.cotizacion.backend.presentation.dto.response;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String username,
        String nombrePersona,
        List<String> roles
) {}
