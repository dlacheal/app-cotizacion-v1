package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Persona") // Respetando mayúsculas del SQL
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Persona implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_persona")
    @SequenceGenerator(name = "seq_persona", sequenceName = "SEQ_PERSONA_AUTO_INCR", allocationSize = 1)
    @Column(name = "ID_PERSONA")
    @EqualsAndHashCode.Include
    private Integer idPersona; // SQL define INTEGER

    @Column(name = "DNI", nullable = false, length = 20, unique = true)
    private String dni;

    @Column(name = "NOMBRE", nullable = false, length = 150)
    private String nombre;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    @Column(name = "EMAIL", length = 100, unique = true)
    private String email;
}
