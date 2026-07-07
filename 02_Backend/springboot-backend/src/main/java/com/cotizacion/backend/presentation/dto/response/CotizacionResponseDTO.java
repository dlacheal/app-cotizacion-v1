package com.cotizacion.backend.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CotizacionResponseDTO(
        Long idCotizacion,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime fecha,

        // Datos aplanados

        // Cliente
        String clienteNombre,
        String clienteRucDni,
        String clienteTelefono,
        String clienteEmail,
        String clienteDireccion,

        // Usuario
        String usuarioUsername,

        // Moneda
        String monedaCodigo,
        String monedaDescripcion,

        // Forma de pago
        String formaPagoNombre,

        // Estado Cotizacion
        String estadoNombre,

        // Otros
        Integer validezDias,
        String observaciones,

        // --- NUEVOS CAMPOS CALCULADOS (Coinciden con Entity) ---
        BigDecimal inversionTotal, // Costo acumulado
        BigDecimal utilidadBrutaTotal, // Ganancia neta
        BigDecimal subTotal, // Venta sin IGV
        BigDecimal montoIgv, // Impuesto calculado
        BigDecimal totalGeneral, // Venta Total (SubTotal + IGV)

        List<CotizacionDetalleResponseDTO> detalles) {
}