package com.devsu.backend.service;

import com.devsu.backend.dto.CuentaRequest;
import com.devsu.backend.entity.Cuenta;

import java.util.List;

public interface CuentaService {
    Cuenta create(CuentaRequest req);
    Cuenta update(Long id, CuentaRequest req);
    Cuenta get(Long id);
    List<Cuenta> list();
    void delete(Long id);
}
