import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_BASE } from '../app.config';

@Injectable({ providedIn: 'root' })
export class ReportesService {
  private url = `${API_BASE}/reportes`;
  constructor(private http: HttpClient) {}

  get(clienteId: string, fechaInicio: string, fechaFin: string) {
    return this.http.get<any>(`${this.url}?clienteId=${clienteId}&fechaInicio=${fechaInicio}&fechaFin=${fechaFin}`);
  }
}
