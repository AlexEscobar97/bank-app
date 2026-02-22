package com.devsu.backend.service.impl;

import com.devsu.backend.dto.ClienteRequest;
import com.devsu.backend.entity.Cliente;
import com.devsu.backend.exception.BusinessException;
import com.devsu.backend.exception.NotFoundException;
import com.devsu.backend.repository.ClienteRepository;
import com.devsu.backend.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repo;

    public ClienteServiceImpl(ClienteRepository repo) {
        this.repo = repo;
    }

    @Override
    public Cliente create(ClienteRequest req) {
        if (repo.existsByClienteId(req.getClienteId())) {
            throw new BusinessException("Ya existe un cliente con clienteId: " + req.getClienteId());
        }
        Cliente c = new Cliente();
        map(req, c);
        return repo.save(c);
    }

    @Override
    public Cliente update(Long id, ClienteRequest req) {
        Cliente c = get(id);
        // si cambia clienteId, validar
        if (!c.getClienteId().equals(req.getClienteId()) && repo.existsByClienteId(req.getClienteId())) {
            throw new BusinessException("Ya existe un cliente con clienteId: " + req.getClienteId());
        }
        map(req, c);
        return repo.save(c);
    }

    @Override
    public Cliente get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado: " + id));
    }

    @Override
    public List<Cliente> list() {
        return repo.findAll();
    }

    @Override
    public void delete(Long id) {
        Cliente c = get(id);
        repo.delete(c);
    }

    private void map(ClienteRequest req, Cliente c) {
        c.setNombre(req.getNombre());
        c.setGenero(req.getGenero());
        c.setEdad(req.getEdad());
        c.setIdentificacion(req.getIdentificacion());
        c.setDireccion(req.getDireccion());
        c.setTelefono(req.getTelefono());

        c.setClienteId(req.getClienteId());
        c.setPassword(req.getPassword());
        c.setEstado(req.getEstado() != null ? req.getEstado() : true);
    }
}
