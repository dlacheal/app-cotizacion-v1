package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cotizacion_detalle")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CotizacionDetalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cotizacion_detalle")
    @SequenceGenerator(name = "seq_cotizacion_detalle", sequenceName = "seq_cotizacion_detalle_auto_incr", allocationSize = 1)
    @Column(name = "id_detalle")
    @EqualsAndHashCode.Include
    private Long idDetalle;

    // --- RELACIONES (Foreign Keys) ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cotizacion", nullable = false)
    private Cotizacion cotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor; // Asegúrate de tener la entidad Proveedor creada

    // --- CAMPOS VISUALES ---

    @Column(name = "numero_linea", nullable = false)
    private Integer numeroLinea;

    @Column(name = "codigo_articulo", length = 50)
    private String codigoArticulo;

    @Column(name = "descripcion_item", columnDefinition = "TEXT")
    private String descripcionItem;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    // --- MÉTRICAS DE CANTIDAD ---

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cantidad = BigDecimal.ONE;

    // --- COSTOS Y MARGEN (Unitarios con 4 decimales) ---

    @Column(name = "costo_unitario", nullable = false, precision = 12, scale = 4)
    private BigDecimal costoUnitario = BigDecimal.ZERO;

    @Column(name = "porcentaje_ganancia", nullable = false, precision = 12, scale = 4)
    private BigDecimal porcentajeGanancia = BigDecimal.ZERO;

    // --- PRECIOS UNITARIOS CALCULADOS (Unitarios con 4 decimales) ---

    @Column(name = "utilidad_unitaria", nullable = false, precision = 12, scale = 4)
    private BigDecimal utilidadUnitaria = BigDecimal.ZERO;

    @Column(name = "precio_venta_unit", nullable = false, precision = 12, scale = 4)
    private BigDecimal precioVentaUnit = BigDecimal.ZERO;

    // --- TOTALES CALCULADOS POR FILA (Totales con 2 decimales) ---

    @Column(name = "costo_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal costoTotal = BigDecimal.ZERO;

    @Column(name = "sub_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(name = "utilidad_bruta", nullable = false, precision = 14, scale = 2)
    private BigDecimal utilidadBruta = BigDecimal.ZERO;

    // --- DATO FINANCIERO COMPRAS ---

    @Column(name = "inversion_total_inc_igv", nullable = false, precision = 14, scale = 2)
    private BigDecimal inversionTotalIncIgv = BigDecimal.ZERO;

    // --- IMPUESTOS Y TOTAL FINAL ---

    @Column(name = "impuesto_porcent", nullable = false, precision = 5, scale = 2)
    private BigDecimal impuestoPorcent = new BigDecimal("18.00");

    @Column(name = "impuesto_monto", nullable = false, precision = 14, scale = 2)
    private BigDecimal impuestoMonto = BigDecimal.ZERO;

    @Column(name = "total_linea", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalLinea = BigDecimal.ZERO;

    // Helper method para la relación bidireccional
    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }
}
