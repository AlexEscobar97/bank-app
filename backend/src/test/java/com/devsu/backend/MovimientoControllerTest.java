package com.devsu.backend;

import com.devsu.backend.dto.MovimientoRequest;
import com.devsu.backend.entity.Cliente;
import com.devsu.backend.entity.Cuenta;
import com.devsu.backend.entity.TipoCuenta;
import com.devsu.backend.entity.TipoMovimiento;
import com.devsu.backend.repository.ClienteRepository;
import com.devsu.backend.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovimientoControllerTest {

    @Autowired MockMvc mvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired ClienteRepository clienteRepo;
    @Autowired CuentaRepository cuentaRepo;

    @BeforeEach
    void setup() {
        cuentaRepo.deleteAll();
        clienteRepo.deleteAll();
    }

    @Test
    void debitoConSaldoCero_debeRetornarSaldoNoDisponible() throws Exception {
        Cliente cli = new Cliente();
        cli.setNombre("Juan");
        cli.setGenero("M");
        cli.setEdad(20);
        cli.setIdentificacion("ID-1");
        cli.setDireccion("Dir");
        cli.setTelefono("7777");
        cli.setClienteId("C1");
        cli.setPassword("1234");
        cli.setEstado(true);
        cli = clienteRepo.save(cli);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("225487");
        cuenta.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuenta.setSaldoInicial(BigDecimal.ZERO);
        cuenta.setSaldoActual(BigDecimal.ZERO);
        cuenta.setEstado(true);
        cuenta.setCliente(cli);
        cuentaRepo.save(cuenta);

        MovimientoRequest req = new MovimientoRequest();
        req.setFecha(LocalDate.now());
        req.setTipoMovimiento(TipoMovimiento.DEBITO);
        req.setValor(new BigDecimal("50"));
        req.setNumeroCuenta("225487");

        mvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void debitoQueExcedeCupoDiario_debeRetornarCupoDiarioExcedido() throws Exception {
        Cliente cli = new Cliente();
        cli.setNombre("Jose");
        cli.setGenero("M");
        cli.setEdad(25);
        cli.setIdentificacion("ID-2");
        cli.setDireccion("Dir");
        cli.setTelefono("8888");
        cli.setClienteId("C2");
        cli.setPassword("1234");
        cli.setEstado(true);
        cli = clienteRepo.save(cli);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("478758");
        cuenta.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta.setSaldoInicial(new BigDecimal("2000"));
        cuenta.setSaldoActual(new BigDecimal("2000"));
        cuenta.setEstado(true);
        cuenta.setCliente(cli);
        cuentaRepo.save(cuenta);

        // retiro 900
        MovimientoRequest r1 = new MovimientoRequest();
        r1.setFecha(LocalDate.now());
        r1.setTipoMovimiento(TipoMovimiento.DEBITO);
        r1.setValor(new BigDecimal("900"));
        r1.setNumeroCuenta("478758");

        mvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(r1)))
                .andExpect(status().isOk());

        // retiro 200 -> excede 1000
        MovimientoRequest r2 = new MovimientoRequest();
        r2.setFecha(LocalDate.now());
        r2.setTipoMovimiento(TipoMovimiento.DEBITO);
        r2.setValor(new BigDecimal("200"));
        r2.setNumeroCuenta("478758");

        mvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(r2)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Cupo diario Excedido"));
    }
}
