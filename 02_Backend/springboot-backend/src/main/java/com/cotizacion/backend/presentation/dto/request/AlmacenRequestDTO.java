package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlmacenRequestDTO(
        @NotBlank(message = "El nombre del almacén es obligatorio")
        @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La dirección es demasiado larga")
        // Aunque en BD es TEXT, limitamos en API por seguridad
        String direccion
) {}
