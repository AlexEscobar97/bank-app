package com.devsu.backend.repository;

import com.devsu.backend.entity.Movimiento;
import com.devsu.backend.entity.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuenta_NumeroCuentaAndFechaBetween(String numeroCuenta, LocalDate inicio, LocalDate fin);

    @Query("""
        select coalesce(sum(abs(m.valor)), 0)
        from Movimiento m
        where m.cuenta.id = :cuentaId
          and m.fecha = :fecha
          and m.tipoMovimiento = :tipo
      """)
    BigDecimal sumAbsByCuentaAndFechaAndTipo(Long cuentaId, LocalDate fecha, TipoMovimiento tipo);
}
