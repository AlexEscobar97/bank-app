import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

type TipoCuenta = 'AHORRO' | 'CORRIENTE';
type TipoMovimiento = 'CREDITO' | 'DEBITO';

type Cuenta = {
  id: number;
  numeroCuenta: string;
  tipoCuenta: TipoCuenta;
};

type MovimientoForm = {
  fecha: string; // YYYY-MM-DD
  tipoMovimiento: TipoMovimiento;
  valor: number;
  numeroCuenta: string; // backend espera ESTO
};

type Movimiento = {
  id: number;
  fecha: string;
  tipoMovimiento: TipoMovimiento;
  valor: number;
  saldoDisponible: number;
  cuenta?: { id: number; numeroCuenta: string }; // puede venir completo
};

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './movimientos.component.html',
  styleUrl: './movimientos.component.css',
})
export class MovimientosComponent {
  api = 'http://localhost:8282/api/movimientos';
  apiCuentas = 'http://localhost:8282/api/cuentas';

  filtro = '';
  errorMsg = '';

  movimientos: Movimiento[] = [];
  cuentas: Cuenta[] = [];

  form: MovimientoForm = this.nuevo();

  constructor(private http: HttpClient) {
    this.cargar();
    this.cargarCuentas();
  }

  nuevo(): MovimientoForm {
    const hoy = new Date().toISOString().slice(0, 10);
    return {
      fecha: hoy,
      tipoMovimiento: 'CREDITO',
      valor: 0,
      numeroCuenta: '',
    };
  }

  cargar() {
    this.errorMsg = '';
    this.http.get<Movimiento[]>(this.api).subscribe({
      next: (data) => (this.movimientos = data),
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al cargar movimientos'),
    });
  }

  cargarCuentas() {
    this.http.get<Cuenta[]>(this.apiCuentas).subscribe({
      next: (data) => (this.cuentas = data),
      error: () => {},
    });
  }

  movimientosFiltrados() {
    const f = this.filtro.toLowerCase().trim();
    if (!f) return this.movimientos;

    return this.movimientos.filter((m) =>
      `${m.fecha} ${m.tipoMovimiento} ${m.valor} ${m.cuenta?.numeroCuenta ?? ''}`
        .toLowerCase()
        .includes(f)
    );
  }

  guardar() {
    this.errorMsg = '';

    // Validación rápida para evitar 400
    if (!this.form.fecha) {
      this.errorMsg = 'Fecha es obligatoria.';
      return;
    }
    if (!this.form.tipoMovimiento) {
      this.errorMsg = 'Tipo de movimiento es obligatorio.';
      return;
    }
    if (this.form.valor === null || this.form.valor === undefined) {
      this.errorMsg = 'Valor es obligatorio.';
      return;
    }
    if (!this.form.numeroCuenta?.trim()) {
      this.errorMsg = 'Debe seleccionar una cuenta.';
      return;
    }

    this.http.post<Movimiento>(this.api, this.form).subscribe({
      next: () => {
        this.form = this.nuevo();
        this.cargar();       // refresca movimientos
        this.cargarCuentas(); // opcional: por si cambió saldo/estado en lista de cuentas
      },
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al guardar'),
    });
  }

  eliminar(id?: number) {
    if (!id) return;

    this.http.delete(`${this.api}/${id}`).subscribe({
      next: () => this.cargar(),
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al eliminar'),
    });
  }

  limpiar() {
    this.form = this.nuevo();
    this.filtro = '';
    this.errorMsg = '';
  }
}