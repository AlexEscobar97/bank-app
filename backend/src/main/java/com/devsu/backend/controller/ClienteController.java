package com.devsu.backend.controller;

import com.devsu.backend.dto.ClienteRequest;
import com.devsu.backend.entity.Cliente;
import com.devsu.backend.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cliente> list() { return service.list(); }

    @GetMapping("/{id}")
    public Cliente get(@PathVariable Long id) { return service.get(id); }

    @PostMapping
    public Cliente create(@Valid @RequestBody ClienteRequest req) { return service.create(req); }

    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @Valid @RequestBody ClienteRequest req) { return service.update(id, req); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
