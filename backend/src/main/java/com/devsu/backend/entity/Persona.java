package com.devsu.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombre;

    @NotBlank
    @Column(nullable = false)
    private String genero;

    @Min(0)
    @Column(nullable = false)
    private Integer edad;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String identificacion;

    @NotBlank

    @Column(nullable = false)
    private String direccion;

    @NotBlank
    @Column(nullable = false)
    private String telefono;
}
