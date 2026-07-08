package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Cotizacion;
import com.cotizacion.backend.presentation.dto.request.CotizacionCreateDTO;
import com.cotizacion.backend.presentation.dto.response.CotizacionResponseDTO;
import com.cotizacion.backend.mapper.CotizacionMapper;
import com.cotizacion.backend.service.interfaces.CotizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cotizaciones")
@RequiredArgsConstructor
public class CotizacionController {

    private final CotizacionService cotizacionService;
    private final CotizacionMapper cotizacionMapper;

    @GetMapping
    public ResponseEntity<Page<CotizacionResponseDTO>> listarTodas(
            @PageableDefault(size = 10, sort = "idCotizacion", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CotizacionResponseDTO> pagina = cotizacionService.findAll(pageable)
                .map(cotizacionMapper::toDto);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<CotizacionResponseDTO>> listarTodosLista() {
        List<CotizacionResponseDTO> lista = cotizacionService.findAll()
                .stream()
                .map(cotizacionMapper::toDto)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CotizacionResponseDTO> obtenerCotizacion(@PathVariable Long id) {
        return cotizacionService.findById(id)
                .map(cotizacionMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CotizacionResponseDTO> crearCotizacion(@RequestBody @Valid CotizacionCreateDTO createDTO) {
        // 1. Mapeo DTO -> Entity (La cabecera y la lista de detalles se crean aquí)
        Cotizacion cotizacion = cotizacionMapper.toEntity(createDTO);

        // 2. Llamada al servicio:
        // Pasamos la entidad y su lista de detalles.
        // El servicio se encarga de:
        // - Vincular (setCotizacion)
        // - Calcular Costos, Inversión, Utilidad y Totales
        Cotizacion nuevaCotizacion = cotizacionService.crearCotizacion(cotizacion, cotizacion.getDetalles());

        // 3. Respuesta con los datos calculados
        return ResponseEntity.status(HttpStatus.CREATED).body(cotizacionMapper.toDto(nuevaCotizacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CotizacionResponseDTO> actualizarCotizacion(
            @PathVariable Long id,
            @RequestBody @Valid CotizacionCreateDTO updateDTO) {

        // 1. Convertimos el DTO a una entidad "temporal" con los nuevos datos
        Cotizacion datosNuevos = cotizacionMapper.toEntity(updateDTO);

        // 2. Llamamos al servicio de actualización
        // Esto recalculará todos los montos (Inversión, IGV, Totales) basándose en los
        // nuevos detalles
        Cotizacion cotizacionActualizada = cotizacionService.actualizarCotizacion(
                id,
                datosNuevos,
                datosNuevos.getDetalles());

        return ResponseEntity.ok(cotizacionMapper.toDto(cotizacionActualizada));
    }

    @PatchMapping("/{id}/estado/{estadoNombre}")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @PathVariable String estadoNombre) {

        cotizacionService.cambiarEstado(id, estadoNombre.toUpperCase());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCotizacion(@PathVariable Long id) {
        if (cotizacionService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        cotizacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}