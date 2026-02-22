import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

type TipoCuenta = 'AHORRO' | 'CORRIENTE';

type Cliente = {
  id?: number;              // puede venir o no, no lo usamos
  nombre: string;
  clienteId: string;        // ESTE es el que usa tu backend en CuentaRequest
  identificacion?: string;
};

type Cuenta = {
  id?: number;
  numeroCuenta: string;
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clienteId: string;        // aquí guardamos "CL-1234"
};

@Component({
  selector: 'app-cuentas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cuentas.component.html',
  styleUrl: './cuentas.component.css',
})
export class CuentasComponent {
  api = 'http://localhost:8282/api/cuentas';
  apiClientes = 'http://localhost:8282/api/clientes';

  filtro = '';
  clienteFiltro = '';
  errorMsg = '';

  cuentas: Cuenta[] = [];
  clientes: Cliente[] = [];

  form: Cuenta = this.nuevo();

  constructor(private http: HttpClient) {
    this.cargar();
    this.cargarClientes();
  }

  nuevo(): Cuenta {
    return {
      numeroCuenta: '',
      tipoCuenta: 'AHORRO',
      saldoInicial: 0,
      estado: true,
      clienteId: '',
    };
  }

  cargar() {
    this.errorMsg = '';
    this.http.get<Cuenta[]>(this.api).subscribe({
      next: (data) => (this.cuentas = data),
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al cargar cuentas'),
    });
  }

  cargarClientes() {
    this.http.get<Cliente[]>(this.apiClientes).subscribe({
      next: (data) => (this.clientes = data),
      error: () => {},
    });
  }

  clientesFiltrados() {
    const f = this.clienteFiltro.toLowerCase().trim();
    if (!f) return this.clientes;

    return this.clientes.filter((c) =>
      `${c.nombre} ${c.clienteId} ${c.identificacion ?? ''}`.toLowerCase().includes(f)
    );
  }

  cuentasFiltradas() {
    const f = this.filtro.toLowerCase().trim();
    if (!f) return this.cuentas;

    return this.cuentas.filter((c) =>
      `${c.numeroCuenta} ${c.tipoCuenta} ${c.clienteId}`.toLowerCase().includes(f)
    );
  }

  guardar() {
    this.errorMsg = '';

    // Validación rápida (evita 400)
    if (!this.form.numeroCuenta?.trim()) {
      this.errorMsg = 'Número de cuenta es obligatorio.';
      return;
    }
    if (!this.form.tipoCuenta) {
      this.errorMsg = 'Tipo de cuenta es obligatorio.';
      return;
    }
    if (this.form.saldoInicial === null || this.form.saldoInicial === undefined) {
      this.errorMsg = 'Saldo inicial es obligatorio.';
      return;
    }
    if (!this.form.clienteId?.trim()) {
      this.errorMsg = 'Debe seleccionar un cliente.';
      return;
    }

    const isUpdate = !!this.form.id;

    const req = isUpdate
      ? this.http.put<Cuenta>(`${this.api}/${this.form.id}`, this.form)
      : this.http.post<Cuenta>(this.api, this.form);

    req.subscribe({
      next: () => {
        this.form = this.nuevo();
        this.cargar();
      },
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al guardar'),
    });
  }

  editar(c: Cuenta) {
    this.form = { ...c };
    // opcional: limpiar filtro de clientes
    // this.clienteFiltro = '';
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
    this.clienteFiltro = '';
    this.errorMsg = '';
  }

  nombreClientePorClienteId(clienteId: string) {
    if (!clienteId) return '-';
    const c = this.clientes.find((x) => x.clienteId === clienteId);
    return c ? `${c.nombre} (${c.clienteId})` : clienteId;
  }
}