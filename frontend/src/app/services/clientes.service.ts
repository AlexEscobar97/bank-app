import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_BASE } from '../app.config';

@Injectable({ providedIn: 'root' })
export class ClientesService {
  private url = `${API_BASE}/clientes`;
  constructor(private http: HttpClient) {}

  list() { return this.http.get<any[]>(this.url); }
  create(body: any) { return this.http.post(this.url, body); }
  update(id: number, body: any) { return this.http.put(`${this.url}/${id}`, body); }
  delete(id: number) { return this.http.delete(`${this.url}/${id}`); }
}
