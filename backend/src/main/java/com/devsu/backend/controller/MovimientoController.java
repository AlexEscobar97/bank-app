package com.devsu.backend.controller;

import com.devsu.backend.dto.MovimientoRequest;
import com.devsu.backend.entity.Movimiento;
import com.devsu.backend.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@CrossOrigin(origins = "*")
public class MovimientoController {

    private final MovimientoService service;

    public MovimientoController(MovimientoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Movimiento> list() { return service.list(); }

    @GetMapping("/{id}")
    public Movimiento get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public Movimiento create(@Valid @RequestBody MovimientoRequest req) { return service.create(req); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
