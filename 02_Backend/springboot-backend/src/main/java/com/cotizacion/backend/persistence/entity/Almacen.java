package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "almacen")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Almacen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_almacen_gen")
    @SequenceGenerator(name = "seq_almacen_gen", sequenceName = "SEQ_ALMACEN_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_almacen")
    @EqualsAndHashCode.Include
    private Long idAlmacen;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String direccion;
}