package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "proveedor")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Proveedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proveedor_gen")
    @SequenceGenerator(name = "seq_proveedor_gen", sequenceName = "SEQ_PROVEEDOR_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_proveedor")
    @EqualsAndHashCode.Include
    private Long idProveedor;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 20)
    private String ruc;

    @Column(length = 30)
    private String telefono;

    @Column(length = 150)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String direccion;
}
