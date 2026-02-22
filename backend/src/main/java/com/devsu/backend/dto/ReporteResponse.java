package com.devsu.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ReporteResponse {
    private String clienteId;
    private String nombreCliente;
    private String fechaInicio;
    private String fechaFin;

    private List<CuentaResumen> cuentas;
    private BigDecimal totalCreditos;
    private BigDecimal totalDebitos;

    private String pdfBase64;

    @Data
    @Builder
    public static class CuentaResumen {
        private String numeroCuenta;
        private String tipoCuenta;
        private BigDecimal saldoActual;
    }
}
