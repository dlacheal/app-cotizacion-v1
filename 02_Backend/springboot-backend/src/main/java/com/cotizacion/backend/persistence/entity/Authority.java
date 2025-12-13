package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Authority", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USUARIO_ID", "TIPO"})
})
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Authority implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_authority")
    @SequenceGenerator(name = "seq_authority", sequenceName = "SEQ_AUTHORITY_AUTO_INCR", allocationSize = 1)
    @Column(name = "ID_AUTHORITY")
    @EqualsAndHashCode.Include
    private Integer idAuthority;

    @Column(name = "TIPO", nullable = false, length = 50)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private Usuario usuario;
}
