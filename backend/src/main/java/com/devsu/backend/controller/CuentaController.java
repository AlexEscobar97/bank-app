package com.devsu.backend.controller;

import com.devsu.backend.dto.CuentaRequest;
import com.devsu.backend.entity.Cuenta;
import com.devsu.backend.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@CrossOrigin(origins = "*")
public class CuentaController {

    private final CuentaService service;

    public CuentaController(CuentaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cuenta> list() { return service.list(); }

    @GetMapping("/{id}")
    public Cuenta get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public Cuenta create(@Valid @RequestBody CuentaRequest req) { return service.create(req); }

    @PutMapping("/{id}")
    public Cuenta update(@PathVariable Long id, @Valid @RequestBody CuentaRequest req) { return service.update(id, req); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
