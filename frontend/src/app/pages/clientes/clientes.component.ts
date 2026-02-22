import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

type Cliente = {
  id?: number;
  nombre: string;
  genero: string;
  edad?: number;
  identificacion: string;
  direccion: string;
  telefono: string;
  clienteId: string;
  password: string;
  estado: boolean;
};

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.css',
})
export class ClientesComponent {
  api = 'http://localhost:8282/api/clientes';

  filtro = '';
  errorMsg = '';
  clientes: Cliente[] = [];

  form: Cliente = this.nuevo();

  constructor(private http: HttpClient) {
    this.cargar();
  }

  nuevo(): Cliente {
    return {
      nombre: '',
      genero: '',
      edad: 0,
      identificacion: '',
      direccion: '',
      telefono: '',
      clienteId: '',
      password: '',
      estado: true,
    };
  }

  cargar() {
    this.errorMsg = '';
    this.http.get<Cliente[]>(this.api).subscribe({
      next: (data) => (this.clientes = data),
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al cargar clientes'),
    });
  }

  clientesFiltrados() {
    const f = this.filtro.toLowerCase().trim();
    if (!f) return this.clientes;

    return this.clientes.filter((c) =>
      `${c.nombre} ${c.clienteId} ${c.identificacion} ${c.telefono}`
        .toLowerCase()
        .includes(f)
    );
  }

  guardar() {
    this.errorMsg = '';

    if (!this.form.nombre?.trim() || !this.form.genero?.trim() || !this.form.identificacion?.trim()
      || !this.form.direccion?.trim() || !this.form.telefono?.trim()
      || !this.form.clienteId?.trim() || !this.form.password?.trim()) {
      this.errorMsg = 'Completa todos los campos obligatorios.';
      return;
    }

    const isUpdate = !!this.form.id;

    const req = isUpdate
      ? this.http.put<Cliente>(`${this.api}/${this.form.id}`, this.form)
      : this.http.post<Cliente>(this.api, this.form);

    req.subscribe({
      next: () => {
        this.form = this.nuevo();
        this.cargar();
      },
      error: (e) => {
        this.errorMsg =
          e?.error?.message ||
          (typeof e?.error === 'string' ? e.error : '') ||
          'Error al guardar';
      },
    });
  }

  editar(c: Cliente) {
    this.form = { ...c };
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