package com.devsu.backend.controller;

import com.devsu.backend.dto.ReporteResponse;
import com.devsu.backend.service.ReporteService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @GetMapping
    public ReporteResponse reporte(
            @RequestParam String clienteId,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin
    ) {
        return service.generar(clienteId, LocalDate.parse(fechaInicio), LocalDate.parse(fechaFin));
    }
}
