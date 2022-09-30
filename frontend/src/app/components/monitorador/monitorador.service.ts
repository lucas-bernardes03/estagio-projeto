import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { Enderecos, Monitorador } from './monitorador.model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, EMPTY, filter, map, Observable, of, Subject } from 'rxjs';
import * as XLSX from 'xlsx'

@Injectable({
  providedIn: 'root'
})
export class MonitoradorService {

  private baseUrl = 'api/monitoradores'

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
    const url = `${this.baseUrl}/m`
    return this.http.get<Monitorador[]>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
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

  retrieveEnderecos(id: number): Observable<Enderecos[]> {
    const url = `${this.baseUrl}/${id}/enderecos`
    return this.http.get<Enderecos[]>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  

  checkIguais(mon: Monitorador): Observable<boolean> {
    var subject = new Subject<boolean>()
    this.read().subscribe(m => {
      var identificacoes: string[] = []
      for(var monitorador of m){
        if(monitorador.tipo === "Física"){
          identificacoes.push(monitorador.cpf!)
          identificacoes.push(monitorador.rg!)
        }
        else{
          identificacoes.push(monitorador.cnpj!)
          identificacoes.push(monitorador.inscricaoEstadual!)
        }
      }

      if(mon.tipo === "Física") subject.next(identificacoes.includes(mon.cpf!) || identificacoes.includes(mon.rg!))
      else subject.next(identificacoes.includes(mon.cnpj!) || identificacoes.includes(mon.inscricaoEstadual!))
    })
    return subject.asObservable()
  }

  checkIguaisUpdate(mon: Monitorador, idCurrent: number): Observable<boolean> {
    var subject = new Subject<boolean>()
     
    this.readById(idCurrent.toString()).subscribe(curr => {
      this.read().subscribe(m => {
        var identificacoes: string[] = []
        for(var monitorador of m){
          if(monitorador.tipo === "Física"){
            identificacoes.push(monitorador.cpf!)
            identificacoes.push(monitorador.rg!)
          }
          else{
            identificacoes.push(monitorador.cnpj!)
            identificacoes.push(monitorador.inscricaoEstadual!)
          }
        }

        var idsFilter

        if(curr.tipo === "Física") idsFilter = identificacoes.filter(data => data !== curr.cpf).filter(data => data !== curr.rg)
        else idsFilter = identificacoes.filter(data => data !== curr.cnpj).filter(data => data !== curr.inscricaoEstadual)

        if(mon.tipo === "Física") subject.next(idsFilter.includes(mon.rg!) || idsFilter.includes(mon.cpf!))
        else subject.next(idsFilter.includes(mon.cnpj!) || idsFilter.includes(mon.inscricaoEstadual!))
      })
    })

    return subject.asObservable()
  }

  toXLSXAll(): void {
    this.read().subscribe(monitoradores => {
      const monF = monitoradores.filter(m => m.tipo === "Física")
      const monJ = monitoradores.filter(m => m.tipo === "Jurídica")

      const monFformat = monF.map(mon => ({
        id: mon.id,
        nome: mon.nome,
        cpf: mon.cpf,
        rg: mon.rg,
        dataNascimento: new Date(mon.dataNascimento!).toLocaleDateString(),
        email: mon.email,
        ativo: mon.ativo ? "Sim":"Não"
      }))

      const monJformat = monJ.map(mon => ({
        id: mon.id,
        razaoSocial: mon.razaoSocial,
        cnpj: mon.cnpj,
        inscricaoEstadual: mon.inscricaoEstadual,
        email: mon.email,
        ativo: mon.ativo ? "Sim":"Não"
      }))
      
      const wsF = XLSX.utils.json_to_sheet(monFformat)
      const wsJ = XLSX.utils.json_to_sheet(monJformat)

      XLSX.utils.sheet_add_aoa(wsF, [["ID", "Nome", "CPF", "RG", "Data de Nascimento", "E-mail", "Ativo"]], { origin: "A1"})
      XLSX.utils.sheet_add_aoa(wsJ, [["ID", "Razão Social", "CNPJ", "Inscrição Estadual", "E-mail", "Ativo"]], { origin: "A1"})

      wsF["!cols"] = [ { wch: 5 }, { wch: 30 }, { wch: 12 }, { wch: 8 }, { wch: 20 }, { wch: 30 }, { wch: 5 } ]
      wsJ["!cols"] = [ { wch: 5 }, { wch: 30 }, { wch: 15 }, { wch: 16 }, { wch: 30 }, { wch: 5 } ]

      const workbook = XLSX.utils.book_new()

      XLSX.utils.book_append_sheet(workbook, wsF, "Pessoa Física")
      XLSX.utils.book_append_sheet(workbook, wsJ, "Pessoa Jurídica")

      XLSX.writeFile(workbook, "Monitoradores.xlsx")
    })
  }

  toXLSXId(id: number): void {
    this.readById(id.toString()).subscribe(m => {
      const workbook = XLSX.utils.book_new()
      
      if(m.tipo === "Física"){
        const wsF = XLSX.utils.aoa_to_sheet([[],[m.id, m.nome, m.cpf, m.rg, new Date(m.dataNascimento!).toLocaleDateString(), m.email, m.ativo ? "Sim":"Não"]])
        XLSX.utils.sheet_add_aoa(wsF, [["ID", "Nome", "CPF", "RG", "Data de Nascimento", "E-mail", "Ativo"]], { origin: "A1"})
        wsF["!cols"] = [ { wch: 5 }, { wch: 30 }, { wch: 12 }, { wch: 8 }, { wch: 20 }, { wch: 30 }, { wch: 5 } ]
        XLSX.utils.book_append_sheet(workbook, wsF, "Monitorador")
      }
      else{
        const wsJ = XLSX.utils.aoa_to_sheet([[],[m.id, m.razaoSocial, m.cnpj, m.inscricaoEstadual, m.email, m.ativo ? "Sim":"Não"]])
        XLSX.utils.sheet_add_aoa(wsJ, [["ID", "Razão Social", "CNPJ", "Inscrição Estadual", "E-mail", "Ativo"]], { origin: "A1"})
        wsJ["!cols"] = [ { wch: 5 }, { wch: 30 }, { wch: 15 }, { wch: 16 }, { wch: 30 }, { wch: 5 } ]
        XLSX.utils.book_append_sheet(workbook, wsJ, "Monitorador")
      }

      this.retrieveEnderecos(id).subscribe(enderecos => {
        const endereceosFormat = enderecos.map(end => ({
          id: end.id,
          endereco: end.endereco,
          numero: end.numero ? end.numero:"S/N",
          cep: end.cep,
          bairro: end.bairro,
          cidade: end.cidade,
          estado: end.estado,
          principal: end.principal ? "Sim":"Não",
          telefone: end.telefone
        }))

        const endS = XLSX.utils.json_to_sheet(endereceosFormat)
        XLSX.utils.sheet_add_aoa(endS, [["ID", "Logradouro", "Número", "CEP", "Bairro", "Cidade", "Estado", "Principal", "Telefone"]], { origin: "A1" })
        endS["!cols"] = [{wch: 5}, {wch: 30}, {wch: 7}, {wch: 9}, {wch: 15}, {wch: 15}, {wch: 7}, {wch: 10}, {wch: 15}]
        XLSX.utils.book_append_sheet(workbook,endS,"Endereços")
        
        XLSX.writeFile(workbook, `${m.nome?.replace(/\s/g, '')}.xlsx`)
      })
    })
  }

  //form validations
  setFormComumValidators(formComum: FormGroup):void {
    formComum.controls['email'].setValidators([Validators.required, Validators.email])
    formComum.controls['ativo'].setValidators(Validators.required)
  }

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

}
