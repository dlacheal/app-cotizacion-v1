package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "cliente")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente")
    @SequenceGenerator(name = "seq_cliente", sequenceName = "SEQ_CLIENTE_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_cliente")
    @EqualsAndHashCode.Include
    private Long idCliente;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "ruc_dni", length = 20)
    private String rucDni;

    @Column(length = 30)
    private String telefono;

    @Column(length = 150)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String direccion;
}