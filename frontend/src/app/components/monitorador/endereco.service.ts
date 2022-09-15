import { FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CEPModel, Enderecos } from './monitorador.model';
import { EMPTY, map, Observable, catchError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EnderecoService {

  cepUrl = 'https://viacep.com.br'
  baseUrl = 'api/enderecos'

  constructor(private http: HttpClient, private snackbar: MatSnackBar) { }

  buscarCEP(cep:string): Observable<CEPModel>{
    const url = `${this.cepUrl}/ws/${cep}/json`
    return this.http.get<CEPModel>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  adicionarNovoM(endereco : Enderecos): Observable<Enderecos>{
    return this.http.post<Enderecos>(this.baseUrl, endereco).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  adicionar(id: number, endereco:Enderecos): Observable<Enderecos> {
    const url = `api/monitoradores/${id}/enderecos`
    return this.http.post<Enderecos>(url,endereco).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  deletar(id:number): Observable<Enderecos>{
    const url = `${this.baseUrl}/${id}`
    return this.http.delete<Enderecos>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)));
  }

  readById(id: number): Observable<Enderecos> {
    const url = `${this.baseUrl}/${id}`
    return this.http.get<Enderecos>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)));
  }

  update(endereco:Enderecos): Observable<Enderecos> {
    const url = `${this.baseUrl}/${endereco.id}`
    return this.http.put<Enderecos>(url, endereco).pipe(map(obj => obj), catchError(e => this.errorHandler(e)));
  }

  errorHandler(e: any): Observable<any> {
    this.showMessage('Ocorreu um erro!', true)
    return EMPTY
  }

  showMessage(msg: string, error: boolean = false): void {
    this.snackbar.open(msg, "x", {
      duration: 3000,
      horizontalPosition: "right",
      verticalPosition: "top",
      panelClass: error ?  ["mat-cancel"]:["mat-success"]
    })
  }

  setFormEnderecoValidators(formEndereco: FormGroup):void {
    formEndereco.controls['endereco'].setValidators([Validators.required])
    formEndereco.controls['numero'].setValidators([Validators.required])
    formEndereco.controls['cep'].setValidators([Validators.required, Validators.pattern('^[0-9]{8}$')])
    formEndereco.controls['bairro'].setValidators([Validators.required])
    formEndereco.controls['telefone'].setValidators([Validators.required])
    formEndereco.controls['cidade'].setValidators([Validators.required])
    formEndereco.controls['estado'].setValidators([Validators.required])
    formEndereco.controls['principal'].setValidators([Validators.required])
  }
}
