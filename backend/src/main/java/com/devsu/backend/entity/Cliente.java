package com.devsu.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "clientes")
public class Cliente extends Persona {

    @NotBlank
    @Column(name = "cliente_id", nullable = false, unique = true)
    private String clienteId;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean estado = true;
}
