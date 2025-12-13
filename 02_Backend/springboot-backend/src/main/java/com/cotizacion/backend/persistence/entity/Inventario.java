package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "inventario", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_producto", "id_almacen"})
})
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inventario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_inventario")
    @SequenceGenerator(name = "seq_inventario", sequenceName = "SEQ_INVENTARIO_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_inventario")
    @EqualsAndHashCode.Include
    private Long idInventario;

    @Column(name = "stock_actual", nullable = false, precision = 14, scale = 2)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;
}