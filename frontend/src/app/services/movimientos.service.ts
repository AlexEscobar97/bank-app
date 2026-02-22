import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_BASE } from '../app.config';

@Injectable({ providedIn: 'root' })
export class MovimientosService {
  private url = `${API_BASE}/movimientos`;
  constructor(private http: HttpClient) {}

  list() { return this.http.get<any[]>(this.url); }
  create(body: any) { return this.http.post(this.url, body); }
  delete(id: number) { return this.http.delete(`${this.url}/${id}`); }
}