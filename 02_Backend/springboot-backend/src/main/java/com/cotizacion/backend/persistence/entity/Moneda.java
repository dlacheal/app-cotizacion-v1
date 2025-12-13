package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "moneda")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Moneda implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_moneda")
    @SequenceGenerator(name = "seq_moneda", sequenceName = "SEQ_MONEDA_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_moneda")
    @EqualsAndHashCode.Include
    private Long idMoneda;

    @Column(nullable = false, length = 10, unique = true)
    private String codigo;

    @Column(length = 50)
    private String descripcion;
}
