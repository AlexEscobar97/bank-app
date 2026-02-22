package com.devsu.backend.service;

import com.devsu.backend.dto.ReporteResponse;

import java.time.LocalDate;

public interface ReporteService {
    ReporteResponse generar(String clienteId, LocalDate inicio, LocalDate fin);
}
