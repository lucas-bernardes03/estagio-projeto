import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Monitorador } from './monitorador.model';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MonitoradorService {

  baseUrl = 'api/monitoradores'

  constructor(private snackbar: MatSnackBar, private http: HttpClient, private formBuilder: FormBuilder) { }

  showMessage(msg: string, error: boolean = false): void {
    this.snackbar.open(msg, "x", {
      duration: 3000,
      horizontalPosition: "right",
      verticalPosition: "top",
      panelClass: error ?  ["mat-cancel"]:["mat-success"]
    })
  }
  
  errorHandler(e: any): Observable<any> {
    this.showMessage('Ocorreu um erro!', true)
    return EMPTY
  }

  create(monitorador: Monitorador): Observable<Monitorador> {
    return this.http.post<Monitorador>(this.baseUrl, monitorador).pipe(map(obj => obj), catchError(e => this.errorHandler(e)));
  }


  read(): Observable<Monitorador[]> {
    return this.http.get<Monitorador[]>(this.baseUrl).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  readById(id:string): Observable<Monitorador>{
    const url = `${this.baseUrl}/${id}`
    return this.http.get<Monitorador>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  update(monitorador: Monitorador): Observable<Monitorador> {
    const url = `${this.baseUrl}/${monitorador.id}`
    return this.http.put<Monitorador>(url, monitorador).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  delete(id: number): Observable<Monitorador>{
    const url = `${this.baseUrl}/${id}`
    return this.http.delete<Monitorador>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  //form validations
  setFormFValidators(formF: FormGroup): void{
    formF.controls['nome'].setValidators([Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(30), Validators.required])
    formF.controls['cpf'].setValidators([Validators.pattern('^[0-9]{11}$'), Validators.required])
    formF.controls['rg'].setValidators([Validators.pattern('^[0-9]{7}$'), Validators.required])
    formF.controls['dataNascimento'].setValidators([Validators.required])
    
    formF.controls['nome'].updateValueAndValidity()
    formF.controls['cpf'].updateValueAndValidity()
    formF.controls['rg'].updateValueAndValidity()
    formF.controls['dataNascimento'].updateValueAndValidity()
  }

  setFormJValidators(formJ: FormGroup): void{
    formJ.controls['razaoSocial'].setValidators([Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(30), Validators.required])
    formJ.controls['cnpj'].setValidators([Validators.pattern('^[0-9]{14}$'), Validators.required])
    formJ.controls['inscricaoEstadual'].setValidators([Validators.pattern('^[0-9]{9}$'), Validators.required])

    formJ.controls['razaoSocial'].updateValueAndValidity()
    formJ.controls['cnpj'].updateValueAndValidity()
    formJ.controls['inscricaoEstadual'].updateValueAndValidity()
  }

  disableFormFValidators(formF: FormGroup):void{
    formF.controls['nome'].clearValidators()
    formF.controls['cpf'].clearValidators()
    formF.controls['rg'].clearValidators()
    formF.controls['dataNascimento'].clearValidators()

    formF.controls['nome'].updateValueAndValidity()
    formF.controls['cpf'].updateValueAndValidity()
    formF.controls['rg'].updateValueAndValidity()
    formF.controls['dataNascimento'].updateValueAndValidity()
  }

  disableFormJValidators(formJ: FormGroup):void{
    formJ.controls['razaoSocial'].clearValidators()
    formJ.controls['cnpj'].clearValidators()
    formJ.controls['inscricaoEstadual'].clearValidators()

    formJ.controls['razaoSocial'].updateValueAndValidity()
    formJ.controls['cnpj'].updateValueAndValidity()
    formJ.controls['inscricaoEstadual'].updateValueAndValidity()
  }

}
