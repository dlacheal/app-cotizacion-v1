package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PersonaRequestDTO(
        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{8}", message = "El DNI debe tener exactamente 8 dígitos numéricos")
        String dni,

        @NotBlank(message = "El nombre completo es obligatorio")
        @Size(min = 2, max = 150, message = "El nombre debe tener entre 2 y 150 caracteres")
        String nombre,

        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
        String telefono,

        @NotBlank(message = "El email es obligatorio") // Generalmente obligatorio para notificaciones
        @Email(message = "Formato de correo inválido")
        @Size(max = 100)
        String email
) {}
