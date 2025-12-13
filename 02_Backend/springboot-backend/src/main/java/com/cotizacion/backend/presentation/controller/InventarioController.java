package com.cotizacion.backend.presentation.controller;

//import com.cotizacion.backend.presentation.dto.request.AjusteStockRequestDTO;
//import com.cotizacion.backend.service.interfaces.InventarioService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/api/v1/inventario")
//@RequiredArgsConstructor
//public class InventarioController {
//    private final InventarioService inventarioService;
//
//    @PatchMapping("/ajustar-stock")
//    public ResponseEntity<?> ajustarStock(@RequestBody @Valid AjusteStockRequestDTO requestDTO) {
//        try {
//            // Se asume que el DTO tiene idProducto, idAlmacen, y cantidad
//            inventarioService.ajustarStock(
//                    requestDTO.idProducto(),
//                    requestDTO.idAlmacen(),
//                    requestDTO.cantidad()
//            );
//            return ResponseEntity.ok("Stock ajustado correctamente.");
//        } catch (RuntimeException e) {
//            // Manejo específico de la excepción "Stock insuficiente"
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//}

import com.cotizacion.backend.persistence.entity.Inventario;
import com.cotizacion.backend.service.interfaces.InventarioService; // O tu package .service
import com.cotizacion.backend.presentation.dto.request.AjusteStockRequestDTO;
import com.cotizacion.backend.presentation.dto.response.InventarioResponseDTO;
import com.cotizacion.backend.mapper.InventarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;
    private final InventarioMapper mapper;

    // ==================== OPERACIONES DE NEGOCIO (EXISTENTE) ====================

    @PatchMapping("/ajustar-stock")
    public ResponseEntity<?> ajustarStock(@RequestBody @Valid AjusteStockRequestDTO requestDTO) {
        try {
            Inventario inventario = inventarioService.ajustarStock(
                    requestDTO.idProducto(),
                    requestDTO.idAlmacen(),
                    requestDTO.cantidad()
            );
            return ResponseEntity.ok(mapper.toDto(inventario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ==================== READ (LECTURAS FILTRADAS) ====================

    /**
     * Ver stock de un producto específico en todos los almacenes.
     * Útil para: "¿Dónde tengo este producto?"
     */
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<InventarioResponseDTO>> verStockPorProducto(@PathVariable Long idProducto) {
        // Asumiendo que agregaste findByProductoId al Service/Repo
        List<InventarioResponseDTO> stock = inventarioService.listarPorProducto(idProducto)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(stock);
    }

    /**
     * Ver todo el inventario de un almacén específico.
     * Útil para: Auditoría de almacén.
     */
    @GetMapping("/almacen/{idAlmacen}")
    public ResponseEntity<List<InventarioResponseDTO>> verStockPorAlmacen(@PathVariable Long idAlmacen) {
        // Asumiendo que agregaste findByAlmacenId al Service/Repo
        List<InventarioResponseDTO> stock = inventarioService.listarPorAlmacen(idAlmacen)
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(stock);
    }

    /**
     * Buscar stock puntual (Producto X en Almacén Y).
     */
    @GetMapping("/consultar")
    public ResponseEntity<InventarioResponseDTO> consultarStockPuntual(
            @RequestParam Long idProducto,
            @RequestParam Long idAlmacen) {

        return inventarioService.findByProductoAndAlmacen(idProducto, idAlmacen)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== DELETE (ADMINISTRATIVO) ====================

    /**
     * Eliminar un registro de inventario.
     * PRECAUCIÓN: Esto borra la relación Producto-Almacén.
     * Generalmente solo se permite si el stock es 0.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRegistroInventario(@PathVariable Long id) {
        // Aquí podrías agregar lógica: "Solo borrar si stock == 0"
        try {
            inventarioService.delete(id); // Asumiendo método delete en Service
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo eliminar el registro: " + e.getMessage());
        }
    }
}
