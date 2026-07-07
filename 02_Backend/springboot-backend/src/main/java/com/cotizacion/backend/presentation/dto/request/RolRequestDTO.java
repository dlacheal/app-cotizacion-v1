package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RolRequestDTO(
        @NotNull(message = "El ID del usuario es obligatorio")
        Integer idUsuario,

        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(max = 50, message = "El rol no puede exceder 50 caracteres")
        String rol
) {}
