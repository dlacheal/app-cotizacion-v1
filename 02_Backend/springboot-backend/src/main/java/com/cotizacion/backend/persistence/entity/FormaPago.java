package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "forma_pago")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FormaPago implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_forma_pago_gen")
    @SequenceGenerator(name = "seq_forma_pago_gen", sequenceName = "SEQ_FORMA_PAGO_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_forma_pago")
    @EqualsAndHashCode.Include
    private Long idFormaPago;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}
