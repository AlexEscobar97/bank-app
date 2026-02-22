package com.devsu.backend.service.impl;

import com.devsu.backend.dto.CuentaRequest;
import com.devsu.backend.entity.Cliente;
import com.devsu.backend.entity.Cuenta;
import com.devsu.backend.exception.BusinessException;
import com.devsu.backend.exception.NotFoundException;
import com.devsu.backend.repository.ClienteRepository;
import com.devsu.backend.repository.CuentaRepository;
import com.devsu.backend.service.CuentaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepo;
    private final ClienteRepository clienteRepo;

    public CuentaServiceImpl(CuentaRepository cuentaRepo, ClienteRepository clienteRepo) {
        this.cuentaRepo = cuentaRepo;
        this.clienteRepo = clienteRepo;
    }

    @Override
    public Cuenta create(CuentaRequest req) {
        if (cuentaRepo.existsByNumeroCuenta(req.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con numeroCuenta: " + req.getNumeroCuenta());
        }
        Cliente cliente = clienteRepo.findByClienteId(req.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado por clienteId: " + req.getClienteId()));

        Cuenta c = new Cuenta();
        c.setNumeroCuenta(req.getNumeroCuenta());
        c.setTipoCuenta(req.getTipoCuenta());
        c.setSaldoInicial(req.getSaldoInicial());
        c.setSaldoActual(req.getSaldoInicial() != null ? req.getSaldoInicial() : BigDecimal.ZERO);
        c.setEstado(req.getEstado() != null ? req.getEstado() : true);
        c.setCliente(cliente);

        return cuentaRepo.save(c);
    }

    @Override
    public Cuenta update(Long id, CuentaRequest req) {
        Cuenta c = get(id);

        if (!c.getNumeroCuenta().equals(req.getNumeroCuenta()) && cuentaRepo.existsByNumeroCuenta(req.getNumeroCuenta())) {
            throw new BusinessException("Ya existe una cuenta con numeroCuenta: " + req.getNumeroCuenta());
        }

        Cliente cliente = clienteRepo.findByClienteId(req.getClienteId())
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado por clienteId: " + req.getClienteId()));

        c.setNumeroCuenta(req.getNumeroCuenta());
        c.setTipoCuenta(req.getTipoCuenta());
        c.setEstado(req.getEstado() != null ? req.getEstado() : c.getEstado());
        c.setCliente(cliente);

        return cuentaRepo.save(c);
    }

    @Override
    public Cuenta get(Long id) {
        return cuentaRepo.findById(id).orElseThrow(() -> new NotFoundException("Cuenta no encontrada: " + id));
    }

    @Override
    public List<Cuenta> list() {
        return cuentaRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        Cuenta c = get(id);
        cuentaRepo.delete(c);
    }
}
