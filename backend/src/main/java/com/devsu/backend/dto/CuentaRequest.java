package com.devsu.backend.dto;

import com.devsu.backend.entity.TipoCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaRequest {
    @NotBlank private String numeroCuenta;
    @NotNull private TipoCuenta tipoCuenta;
    @NotNull private BigDecimal saldoInicial;
    private Boolean estado = true;

    @NotBlank private String clienteId;
}