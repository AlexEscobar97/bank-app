package com.devsu.backend.service;

import com.devsu.backend.dto.MovimientoRequest;
import com.devsu.backend.entity.Movimiento;

import java.util.List;

public interface MovimientoService {
    Movimiento create(MovimientoRequest req);
    Movimiento get(Long id);
    List<Movimiento> list();
    void delete(Long id);
}
