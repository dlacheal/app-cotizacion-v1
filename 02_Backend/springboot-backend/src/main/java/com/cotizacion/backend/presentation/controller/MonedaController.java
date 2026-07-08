package com.cotizacion.backend.presentation.controller;

import com.cotizacion.backend.persistence.entity.Moneda;
import com.cotizacion.backend.service.interfaces.MonedaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/monedas")
@RequiredArgsConstructor
public class MonedaController {
    private final MonedaService monedaService;

    @GetMapping
    public ResponseEntity<Page<Moneda>> listarMonedas(
            @PageableDefault(size = 10, sort = "idMoneda") Pageable pageable) {
        return ResponseEntity.ok(monedaService.findAll(pageable));
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Moneda>> listarTodas() {
        return ResponseEntity.ok(monedaService.findAll());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Moneda> buscarPorId(@PathVariable Long id) {
        return monedaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Moneda> buscarPorCodigo(@PathVariable String codigo) {
        return monedaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearMoneda(@RequestBody @Valid Moneda moneda) {
        if (monedaService.findByCodigo(moneda.getCodigo()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El código de moneda '" + moneda.getCodigo() + "' ya se encuentra registrado.");
        }
        Moneda guardada = monedaService.save(moneda);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarMoneda(@PathVariable Long id, @RequestBody @Valid Moneda monedaDatos) {
        return monedaService.findById(id)
                .map(monedaExistente -> {
                    var otraMoneda = monedaService.findByCodigo(monedaDatos.getCodigo());
                    if (otraMoneda.isPresent() && !otraMoneda.get().getIdMoneda().equals(id)) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("El código de moneda '" + monedaDatos.getCodigo() + "' ya se encuentra registrado por otra moneda.");
                    }
                    monedaExistente.setCodigo(monedaDatos.getCodigo());
                    monedaExistente.setDescripcion(monedaDatos.getDescripcion());
                    return ResponseEntity.ok(monedaService.save(monedaExistente));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMoneda(@PathVariable Long id) {
        if (monedaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        monedaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
