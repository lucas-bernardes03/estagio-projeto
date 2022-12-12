import { FormGroup, Validators } from '@angular/forms';
import { Enderecos, Monitorador } from './monitorador.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, map, Observable, Subject } from 'rxjs';
import * as XLSX from 'xlsx'

@Injectable({
  providedIn: 'root'
})
export class MonitoradorService {

  private baseUrl = 'api/monitoradores'

  constructor(private snackbar: MatSnackBar, private http: HttpClient) { }

  showMessage(msg: string, error: boolean = false): void {
    this.snackbar.open(msg, "x", {
      duration: 3000,
      horizontalPosition: "right",
      verticalPosition: "top",
      panelClass: error ?  ["mat-cancel"]:["mat-success"]
    })
  }

  errorHandler(e: any): Observable<any> {
    this.showMessage('Erro de conexão com o banco de dados!', true)
    console.log(e)
    return EMPTY
  }

  create(monitorador: Monitorador): Observable<Monitorador> {
    return this.http.post<Monitorador>(this.baseUrl, monitorador).pipe(map(obj => obj), catchError(e => this.errorHandler(e)));
  }

  createMany(monitoradores: Monitorador[]): Observable<Monitorador[]> {
    const url = `${this.baseUrl}/m`
    return this.http.post<Monitorador[]>(url, monitoradores).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  downloadPdf(): Observable<Blob> {
    const url = `${this.baseUrl}/pdf`
    return this.http.get(url, { responseType: 'blob' }).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  downloadExcelAll(): Observable<Blob> {
    const url = `${this.baseUrl}/excel`
    return this.http.get(url, { responseType: 'blob' }).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  downloadExcelId(id: number): Observable<Blob>{
    const url = `${this.baseUrl}/${id}/excel`
    return this.http.get(url, { responseType: 'blob' }).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  downloadModeloExcel(): Observable<Blob> {
    const url = `${this.baseUrl}/modelo`
    return this.http.get(url, { responseType: 'blob'}).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  readPaginated(params: HttpParams) {
    return this.http.get<Monitorador[]>(this.baseUrl, { params: params }).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
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

  retrieveEnderecosPageable(id: string, params: HttpParams){
    const url = `${this.baseUrl}/${id}/enderecos`
    return this.http.get<Enderecos[]>(url, {params: params}).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  readExcel(file: File): Observable<Monitorador[]>{
    const reader = new FileReader();
    const monitoradores: Monitorador[] = [];

    const subject = new Subject<Monitorador[]>();

    reader.onload = () => {
      let data = reader.result
      const workbook = XLSX.read(data, {type: 'binary'})

      workbook.SheetNames.forEach(sheetName => {
        let rows = XLSX.utils.sheet_to_json(workbook.Sheets[sheetName])

        if(sheetName === 'Cadastrar'){
          rows.forEach((row:any) => {
            const mon:Monitorador = {
              id: null,
              tipo: null,
              nome: null,
              razaoSocial: null,
              cpf: null,
              cnpj: null,
              rg: null,
              inscricaoEstadual: null,
              dataNascimento: null,
              email: null,
              ativo: true,
              enderecos: []
            }

            const end:Enderecos = {
              id: null,
              endereco: null,
              numero: null,
              cep: null,
              bairro: null,
              telefone: null,
              cidade: null,
              estado: null,
              principal: true
            }

            mon.tipo = row['Tipo (Física ou Jurídica)']
            mon.email = row["E-mail"]

            if(mon.tipo === "Física"){
              mon.nome = row["Nome / Razão Social"]
              mon.cpf = row['CPF / CNPJ']
              mon.rg = row["RG / Iscrição Estadual"]
              mon.dataNascimento = row["Data de Nascimento"]
            }
            else{
              mon.razaoSocial = row["Nome / Razão Social"]
              mon.cnpj = row['CPF / CNPJ']
              mon.inscricaoEstadual = row["RG / Iscrição Estadual"]
            }

            end.endereco = row['Logradouro']
            end.numero = row['Número']
            end.cep = row['CEP']
            end.bairro = row['Bairro']
            end.cidade = row['Cidade']
            end.estado = row['Estado']
            end.telefone = MonitoradorService.formatPhone(row['Telefone'])

            mon.enderecos?.push(end)
            monitoradores.push(mon)
          })
        }

        else{
          this.showMessage("Formato inválido de arquivo.", true)
        }
      })
      subject.next(monitoradores)
    }

    reader.onerror = function(e){
      console.log(e)
    }

    reader.readAsBinaryString(file)

    return subject.asObservable()
  }

  //form validations
  setFormComumValidators(formComum: FormGroup):void {
    formComum.controls['email'].setValidators([Validators.required, Validators.email])
    formComum.controls['ativo'].setValidators(Validators.required)
  }

  setFormFValidators(formF: FormGroup): void{
    formF.controls['nome'].setValidators([Validators.pattern('^[A-Za-zÀ-ÖØ-öø-ÿ ]*$'), Validators.maxLength(50), Validators.required])
    formF.controls['cpf'].setValidators([Validators.pattern('^[0-9]{11}$'), Validators.required])
    formF.controls['rg'].setValidators([Validators.pattern('^[0-9]{7}$'), Validators.required])
    formF.controls['dataNascimento'].setValidators([Validators.required])

    formF.controls['nome'].updateValueAndValidity()
    formF.controls['cpf'].updateValueAndValidity()
    formF.controls['rg'].updateValueAndValidity()
    formF.controls['dataNascimento'].updateValueAndValidity()
  }

  setFormJValidators(formJ: FormGroup): void{
    formJ.controls['razaoSocial'].setValidators([Validators.pattern('^[A-Za-zÀ-ÖØ-öø-ÿ ]*$'), Validators.maxLength(50), Validators.required])
    formJ.controls['cnpj'].setValidators([Validators.pattern('^[0-9]{14}$'), Validators.required])
    formJ.controls['inscricaoEstadual'].setValidators([Validators.pattern('^[0-9]{12}$'), Validators.required])

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

  public validateNome(mon: Monitorador): boolean {
    if(mon.tipo == 'Física') return /^[A-Za-zÀ-ÖØ-öø-ÿ ]{1,50}$/.test(mon.nome!)
    else return /^[A-Za-zÀ-ÖØ-öø-ÿ ]{1,50}$/.test(mon.razaoSocial!)
  }

  public validateCadastro(mon: Monitorador): boolean {
    if(mon.tipo == 'Física') return /^\d{11}$/.test(mon.cpf!)
    else return /^\d{14}$/.test(mon.cnpj!)
  }

  public validateRegistro(mon : Monitorador): boolean {
    if(mon.tipo == 'Física') return /^\d{7}$/.test(mon.rg!)
    else return /^\d{12}$/.test(mon.inscricaoEstadual!)
  }

  private static formatPhone(phone:number):string {
    return phone.toString().replace(/^(\d{0,2})(\d{0,9})$/, '($1) $2')
  }

}

