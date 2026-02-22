package com.devsu.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteRequest {
    @NotBlank private String nombre;
    @NotBlank private String genero;
    @Min(0) private Integer edad;
    @NotBlank private String identificacion;
    @NotBlank private String direccion;
    @NotBlank private String telefono;

    @NotBlank private String clienteId;
    @NotBlank private String password;
    private Boolean estado = true;
}
