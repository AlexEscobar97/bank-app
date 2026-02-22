package com.devsu.backend.service;

import com.devsu.backend.dto.ClienteRequest;
import com.devsu.backend.entity.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente create(com.devsu.backend.dto.ClienteRequest req);
    Cliente update(Long id, ClienteRequest req);
    Cliente get(Long id);
    List<Cliente> list();
    void delete(Long id);
}
