import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';

type Cliente = {
  nombre: string;
  clienteId: string; // CL-1234
  identificacion?: string;
};

type CuentaResumen = {
  numeroCuenta: string;
  tipoCuenta: string;
  saldoActual: number;
};

type ReporteResponse = {
  clienteId: string;
  nombreCliente: string;
  fechaInicio: string;
  fechaFin: string;
  cuentas: CuentaResumen[];
  totalCreditos: number;
  totalDebitos: number;
  pdfBase64: string;
};

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './reportes.component.html',
  styleUrl: './reportes.component.css',
})
export class ReportesComponent {
  api = 'http://localhost:8282/api/reportes';
  apiClientes = 'http://localhost:8282/api/clientes';

  errorMsg = '';
  clientes: Cliente[] = [];

  clienteId: string = '';
  fechaInicio = '2022-02-01';
  fechaFin = '2022-02-28';

  reporte?: ReporteResponse;

  constructor(private http: HttpClient) {
    this.cargarClientes();
  }

  cargarClientes() {
    // OJO: el backend /clientes devuelve entidad Cliente completa, al menos trae nombre y clienteId
    this.http.get<any[]>(this.apiClientes).subscribe({
      next: (d) => {
        // Mapeo defensivo para adaptarse a tu JSON real
        this.clientes = (d || []).map((x) => ({
          nombre: x.nombre ?? x.nombres ?? '',
          clienteId: x.clienteId ?? '',
          identificacion: x.identificacion,
        })).filter(c => !!c.clienteId);
      },
      error: () => {},
    });
  }

  consultar() {
    this.errorMsg = '';
    this.reporte = undefined;

    if (!this.clienteId?.trim()) {
      this.errorMsg = 'Selecciona un cliente.';
      return;
    }
    if (!this.fechaInicio || !this.fechaFin) {
      this.errorMsg = 'Selecciona fecha inicio y fin.';
      return;
    }

    const params = new HttpParams()
      .set('clienteId', this.clienteId)
      .set('fechaInicio', this.fechaInicio)
      .set('fechaFin', this.fechaFin);

    this.http.get<ReporteResponse>(this.api, { params }).subscribe({
      next: (d) => (this.reporte = d),
      error: (e) => (this.errorMsg = e?.error?.message || 'Error al consultar reporte'),
    });
  }

  descargarPdf() {
    if (!this.reporte?.pdfBase64) return;

    const byteCharacters = atob(this.reporte.pdfBase64);
    const byteNumbers = new Array(byteCharacters.length);

    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }

    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: 'application/pdf' });

    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;

    const nombre = `reporte_${this.reporte.clienteId}_${this.reporte.fechaInicio}_${this.reporte.fechaFin}.pdf`;
    a.download = nombre;

    document.body.appendChild(a);
    a.click();
    a.remove();
    URL.revokeObjectURL(url);
  }

  limpiar() {
    this.reporte = undefined;
    this.errorMsg = '';
  }
}