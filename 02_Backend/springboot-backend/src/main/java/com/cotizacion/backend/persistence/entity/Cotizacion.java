package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cotizacion")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cotizacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cotizacion")
    @SequenceGenerator(name = "seq_cotizacion", sequenceName = "seq_cotizacion_auto_incr", allocationSize = 1)
    @Column(name = "id_cotizacion")
    @EqualsAndHashCode.Include
    private Long idCotizacion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "validez_dias", nullable = false)
    private Integer validezDias = 7;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    // --- NUEVOS CAMPOS SEGÚN AJUSTE DE TABLA ---

    @Column(name = "inversion_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal inversionTotal = BigDecimal.ZERO;

    @Column(name = "utilidad_bruta_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal utilidadBrutaTotal = BigDecimal.ZERO;

    @Column(name = "sub_total", nullable = false, precision = 14, scale = 2)
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Column(name = "monto_igv", nullable = false, precision = 14, scale = 2)
    private BigDecimal montoIgv = BigDecimal.ZERO;

    @Column(name = "total_general", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalGeneral = BigDecimal.ZERO;

    // ---------------------------------------------

    // Relaciones (Foreign Keys)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_moneda", nullable = false)
    private Moneda moneda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_forma_pago")
    private FormaPago formaPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado", nullable = false)
    private EstadoCotizacion estado;

    // Relación Hijo (Detalle)
    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CotizacionDetalle> detalles = new ArrayList<>();

    // Helper method para mantener consistencia bidireccional
    public void addDetalle(CotizacionDetalle detalle) {
        detalles.add(detalle);
        detalle.setCotizacion(this);
    }

    public void removeDetalle(CotizacionDetalle detalle) {
        detalles.remove(detalle);
        detalle.setCotizacion(null);
    }

    // Lifecycle hook para poner fecha por defecto si no viene
    @PrePersist
    public void prePersist() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
