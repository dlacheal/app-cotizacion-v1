package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MonedaRequestDTO(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 10, message = "El código no puede exceder 10 caracteres")
        String codigo,

        @Size(max = 50, message = "La descripción no puede exceder 50 caracteres")
        String descripcion
) {}
