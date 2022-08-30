import { Monitorador } from './monitorador.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MonitoradorService {

  baseUrl = 'api/monitoradores'

  constructor(private snackbar: MatSnackBar, private http: HttpClient) { }

  showMessage(msg: string): void {
    this.snackbar.open(msg, "x", {
      duration: 3000,
      horizontalPosition: "right",
      verticalPosition: "top",
      panelClass: ["mat-success"]
    })
  }

  create(monitorador: Monitorador): Observable<Monitorador> {
    return this.http.post<Monitorador>(this.baseUrl, monitorador)
  }

  read(): Observable<Monitorador[]> {
    return this.http.get<Monitorador[]>(this.baseUrl)
  }

  readById(id:string): Observable<Monitorador>{
    const url = `${this.baseUrl}/${id}`
    return this.http.get<Monitorador>(url)
  }

  update(monitorador: Monitorador): Observable<Monitorador> {
    const url = `${this.baseUrl}/${monitorador.id}`
    return this.http.put<Monitorador>(url, monitorador)
  }

}
