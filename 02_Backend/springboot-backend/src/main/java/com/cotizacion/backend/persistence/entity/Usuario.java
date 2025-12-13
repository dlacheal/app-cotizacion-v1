package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "SEQ_USUARIO_AUTO_INCR", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    @EqualsAndHashCode.Include
    private Integer idUsuario;

    @Column(name = "USERNAME", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ENABLE", nullable = false)
    private boolean enable = true;

    // Relación Unidireccional suficiente para este caso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PERSONA_ID", nullable = false)
    private Persona persona;

    // Opcional: Para cargar roles automáticamente
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Authority> authorities;
}
