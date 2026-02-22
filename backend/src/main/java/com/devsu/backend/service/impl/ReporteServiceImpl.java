package com.devsu.backend.service.impl;

import com.devsu.backend.dto.ReporteResponse;
import com.devsu.backend.entity.Cuenta;
import com.devsu.backend.entity.Movimiento;
import com.devsu.backend.entity.TipoMovimiento;
import com.devsu.backend.exception.NotFoundException;
import com.devsu.backend.repository.ClienteRepository;
import com.devsu.backend.repository.CuentaRepository;
import com.devsu.backend.repository.MovimientoRepository;
import com.devsu.backend.service.ReporteService;
import com.devsu.backend.util.PdfReportUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private final ClienteRepository clienteRepo;
    private final CuentaRepository cuentaRepo;
    private final MovimientoRepository movRepo;

    public ReporteServiceImpl(ClienteRepository clienteRepo, CuentaRepository cuentaRepo, MovimientoRepository movRepo) {
        this.clienteRepo = clienteRepo;
        this.cuentaRepo = cuentaRepo;
        this.movRepo = movRepo;
    }

    @Override
    public ReporteResponse generar(String clienteId, LocalDate inicio, LocalDate fin) {
        var cliente = clienteRepo.findByClienteId(clienteId)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado por clienteId: " + clienteId));

        List<Cuenta> cuentas = cuentaRepo.findByCliente_ClienteId(clienteId);

        BigDecimal totalCreditos = BigDecimal.ZERO;
        BigDecimal totalDebitos = BigDecimal.ZERO;

        for (Cuenta c : cuentas) {
            List<Movimiento> movs = movRepo.findByCuenta_NumeroCuentaAndFechaBetween(c.getNumeroCuenta(), inicio, fin);
            for (Movimiento m : movs) {
                if (m.getTipoMovimiento() == TipoMovimiento.CREDITO) totalCreditos = totalCreditos.add(m.getValor().abs());
                if (m.getTipoMovimiento() == TipoMovimiento.DEBITO) totalDebitos = totalDebitos.add(m.getValor().abs());
            }
        }

        byte[] pdfBytes = PdfReportUtil.buildPdf(cliente, cuentas, inicio, fin, totalCreditos, totalDebitos, movRepo);
        String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

        return ReporteResponse.builder()
                .clienteId(cliente.getClienteId())
                .nombreCliente(cliente.getNombre())
                .fechaInicio(inicio.toString())
                .fechaFin(fin.toString())
                .totalCreditos(totalCreditos)
                .totalDebitos(totalDebitos)
                .cuentas(
                        cuentas.stream().map(c ->
                                ReporteResponse.CuentaResumen.builder()
                                        .numeroCuenta(c.getNumeroCuenta())
                                        .tipoCuenta(c.getTipoCuenta().name())
                                        .saldoActual(c.getSaldoActual())
                                        .build()
                        ).toList()
                )
                .pdfBase64(pdfBase64)
                .build();
    }
}
