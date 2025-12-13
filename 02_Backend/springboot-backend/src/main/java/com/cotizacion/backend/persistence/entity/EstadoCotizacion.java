package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "estado_cotizacion")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EstadoCotizacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estado_cot_gen")
    @SequenceGenerator(name = "seq_estado_cot_gen", sequenceName = "SEQ_ESTADO_COTIZACION_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_estado")
    @EqualsAndHashCode.Include
    private Long idEstado;

    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
