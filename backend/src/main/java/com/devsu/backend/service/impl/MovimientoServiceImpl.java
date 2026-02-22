package com.devsu.backend.service.impl;

import com.devsu.backend.dto.MovimientoRequest;
import com.devsu.backend.entity.Cuenta;
import com.devsu.backend.entity.Movimiento;
import com.devsu.backend.entity.TipoMovimiento;
import com.devsu.backend.exception.BusinessException;
import com.devsu.backend.exception.NotFoundException;
import com.devsu.backend.repository.CuentaRepository;
import com.devsu.backend.repository.MovimientoRepository;
import com.devsu.backend.service.MovimientoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movRepo;
    private final CuentaRepository cuentaRepo;

    @Value("${bank.daily-withdraw-limit:1000}")
    private BigDecimal dailyLimit;

    public MovimientoServiceImpl(MovimientoRepository movRepo, CuentaRepository cuentaRepo) {
        this.movRepo = movRepo;
        this.cuentaRepo = cuentaRepo;
    }

    @Override
    public Movimiento create(MovimientoRequest req) {
        Cuenta cuenta = cuentaRepo.findByNumeroCuenta(req.getNumeroCuenta())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada por numeroCuenta: " + req.getNumeroCuenta()));

        BigDecimal saldoActual = cuenta.getSaldoActual() != null ? cuenta.getSaldoActual() : BigDecimal.ZERO;

        BigDecimal valor = req.getValor();
        if (valor == null) throw new BusinessException("Valor no puede ser nulo");

        if (req.getTipoMovimiento() == TipoMovimiento.DEBITO && valor.signum() > 0) {
            valor = valor.negate();
        }
        if (req.getTipoMovimiento() == TipoMovimiento.CREDITO && valor.signum() < 0) {
            valor = valor.abs();
        }

        // Regla: saldo 0 + débito -> Saldo no disponible
        if (req.getTipoMovimiento() == TipoMovimiento.DEBITO && saldoActual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Saldo no disponible");
        }

        // Regla: cupo diario (solo débitos)
        if (req.getTipoMovimiento() == TipoMovimiento.DEBITO) {
            BigDecimal usedToday = movRepo.sumAbsByCuentaAndFechaAndTipo(cuenta.getId(), req.getFecha(), TipoMovimiento.DEBITO);
            BigDecimal newAbs = valor.abs();
            if (usedToday.add(newAbs).compareTo(dailyLimit) > 0) {
                throw new BusinessException("Cupo diario Excedido");
            }
        }

        BigDecimal nuevoSaldo = saldoActual.add(valor);

        if (req.getTipoMovimiento() == TipoMovimiento.DEBITO && nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        Movimiento mov = new Movimiento();
        mov.setFecha(req.getFecha() != null ? req.getFecha() : LocalDate.now());
        mov.setTipoMovimiento(req.getTipoMovimiento());
        mov.setValor(valor);
        mov.setSaldoDisponible(nuevoSaldo);
        mov.setCuenta(cuenta);

        Movimiento saved = movRepo.save(mov);
        cuenta.setSaldoActual(nuevoSaldo);
        cuentaRepo.save(cuenta);

        return saved;
    }

    @Override
    public Movimiento get(Long id) {
        return movRepo.findById(id).orElseThrow(() -> new NotFoundException("Movimiento no encontrado: " + id));
    }

    @Override
    public List<Movimiento> list() {
        return movRepo.findAll();
    }

    @Override
    public void delete(Long id) {
        Movimiento m = get(id);
        movRepo.delete(m);
    }
}