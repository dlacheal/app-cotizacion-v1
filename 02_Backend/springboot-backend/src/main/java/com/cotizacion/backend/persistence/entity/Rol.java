package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Rol")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Rol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rol")
    @SequenceGenerator(name = "seq_rol", sequenceName = "SEQ_ROL_AUTO_INCR", allocationSize = 1)
    @Column(name = "ID_ROL")
    @EqualsAndHashCode.Include
    private Integer idRol;

    @Column(name = "NOMBRE", nullable = false, unique = true, length = 50)
    private String nombre;
}
