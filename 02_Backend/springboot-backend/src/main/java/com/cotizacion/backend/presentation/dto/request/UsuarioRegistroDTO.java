package com.cotizacion.backend.presentation.dto.request;

import jakarta.validation.constraints.*;

public record UsuarioRegistroDTO(
        @NotBlank(message = "El username es obligatorio")
        @Size(min = 4, max = 50, message = "El username debe tener entre 4 y 50 caracteres")
        String username,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        // Opcional: Regex para forzar mayúsculas/números
        // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$", message = "Contraseña insegura")
        String password,

        @NotNull(message = "Debes vincular el usuario a una Persona existente")
        Integer idPersona,

        // Opcional: Lista de roles iniciales (Ej: ["ADMIN", "VENDEDOR"])
        // Si se omite, el backend asigna "USER" por defecto
        String[] roles
) {}
