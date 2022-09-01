import { FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CEPModel } from './monitorador.model';
import { EMPTY, map, Observable, catchError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EnderecoService {

  baseUrl = 'https://viacep.com.br'

  constructor(private http: HttpClient, private snackbar: MatSnackBar) { }

  buscarCEP(cep:string): Observable<CEPModel>{
    const url = `${this.baseUrl}/ws/${cep}/json`
    return this.http.get<CEPModel>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
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
