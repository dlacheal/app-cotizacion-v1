package com.cotizacion.backend.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "categoria")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categoria")
    @SequenceGenerator(name = "seq_categoria", sequenceName = "SEQ_CATEGORIA_AUTO_INCR", allocationSize = 1)
    @Column(name = "id_categoria")
    @EqualsAndHashCode.Include
    private Long idCategoria;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String nombre;

    @Size(max = 250, message = "La descripción no puede exceder los 250 caracteres")
    @Column(columnDefinition = "TEXT")
    private String descripcion;
}