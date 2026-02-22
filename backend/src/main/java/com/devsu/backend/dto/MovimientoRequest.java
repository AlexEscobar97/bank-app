package com.devsu.backend.dto;

import com.devsu.backend.entity.TipoMovimiento;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovimientoRequest {
    @NotNull private LocalDate fecha;
    @NotNull private TipoMovimiento tipoMovimiento;
    @NotNull private BigDecimal valor;
    @NotNull private String numeroCuenta;
}
