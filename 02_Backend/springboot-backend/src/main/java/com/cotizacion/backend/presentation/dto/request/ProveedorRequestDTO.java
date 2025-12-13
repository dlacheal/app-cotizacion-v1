package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ProveedorRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
        String nombre,

        @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos numéricos")
        String ruc,

        @Size(max = 30, message = "El teléfono no puede exceder 30 caracteres")
        String telefono,

        @Email(message = "Formato de email inválido")
        @Size(max = 150)
        String email,

        @Size(max = 2000, message = "La dirección es demasiado larga") // Asumiendo TEXT
        String direccion
) {}
