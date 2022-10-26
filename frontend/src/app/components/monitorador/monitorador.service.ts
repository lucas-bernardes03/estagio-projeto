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

  read(): Observable<Monitorador[]> {
    const url = `${this.baseUrl}/m`
    return this.http.get<Monitorador[]>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  downloadPdf(): Observable<Blob> {
    const url = `${this.baseUrl}/pdf`
    return this.http.get(url, { responseType: 'blob' }).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
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

  retrieveEnderecos(id: number): Observable<Enderecos[]> {
    const url = `${this.baseUrl}/${id}/enderecos/m`
    return this.http.get<Enderecos[]>(url).pipe(map(obj => obj), catchError(e => this.errorHandler(e)))
  }

  getIdentificacoes(): Observable<string[]> {
    const subject = new Subject<string[]>()
    this.read().subscribe(m => {
      const identificacoes: string[] = []
      for (let monitorador of m) {
        if (monitorador.tipo === "Física") {
          identificacoes.push(monitorador.cpf!)
          identificacoes.push(monitorador.rg!)
        } else {
          identificacoes.push(monitorador.cnpj!)
          identificacoes.push(monitorador.inscricaoEstadual!)
        }
      }
      subject.next(identificacoes);
    })
    return subject.asObservable();
  }


  checkIguais(mon: Monitorador): Observable<boolean> {
    const subject = new Subject<boolean>()
    this.getIdentificacoes().subscribe(ids => {
      if(mon.tipo === "Física") subject.next(ids.includes(mon.cpf!) || ids.includes(mon.rg!))
      else subject.next(ids.includes(mon.cnpj!) || ids.includes(mon.inscricaoEstadual!))
    })

    return subject.asObservable()
  }

  checkIguaisUpdate(mon: Monitorador, idCurrent: number): Observable<boolean> {
    const subject = new Subject<boolean>()

    this.readById(idCurrent.toString()).subscribe(curr => {
      this.getIdentificacoes().subscribe(ids => {

        let idsFilter

        if(curr.tipo === "Física") idsFilter = ids.filter(data => data !== curr.cpf).filter(data => data !== curr.rg)
        else idsFilter = ids.filter(data => data !== curr.cnpj).filter(data => data !== curr.inscricaoEstadual)

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

      wsF["!cols"] = [ { wch: 5 }, { wch: 30 }, { wch: 12 }, { wch: 10 }, { wch: 20 }, { wch: 30 }, { wch: 5 } ]
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
        const wsF = XLSX.utils.aoa_to_sheet([[],[m.id, m.tipo, m.nome, m.cpf, m.rg, new Date(m.dataNascimento!).toLocaleDateString(), m.email, m.ativo ? "Sim":"Não"]])
        XLSX.utils.sheet_add_aoa(wsF, [["ID", "Tipo", "Nome", "CPF", "RG", "Data de Nascimento", "E-mail", "Ativo"]], { origin: "A1"})
        wsF["!cols"] = [{ wch: 5 }, {wch: 7}, { wch: 30 }, { wch: 12 }, { wch: 8 }, { wch: 20 }, { wch: 30 }, { wch: 5 } ]
        XLSX.utils.book_append_sheet(workbook, wsF, "Monitorador")
      }
      else{
        const wsJ = XLSX.utils.aoa_to_sheet([[],[m.id, m.tipo, m.razaoSocial, m.cnpj, m.inscricaoEstadual, m.email, m.ativo ? "Sim":"Não"]])
        XLSX.utils.sheet_add_aoa(wsJ, [["ID", "Tipo", "Razão Social", "CNPJ", "Inscrição Estadual", "E-mail", "Ativo"]], { origin: "A1"})
        wsJ["!cols"] = [{ wch: 5 }, {wch: 9},  { wch: 30 }, { wch: 15 }, { wch: 16 }, { wch: 30 }, { wch: 5 } ]
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

        if(m.tipo === 'Física') XLSX.writeFile(workbook, `${m.nome?.replace(/\s/g, '')}.xlsx`)
        else XLSX.writeFile(workbook, `${m.razaoSocial?.replace(/\s/g, '')}.xlsx`)
      })
    })
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

        let ids: string[] = []
        this.getIdentificacoes().subscribe(r => {ids = r})

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

            if (MonitoradorService.validateExcelMonitorador(mon) && MonitoradorService.validataExcelEndereco(end)){
              if(mon.tipo == 'Física' && ids.includes(mon.rg!)
                || mon.tipo == 'Física' && ids.includes(mon.cpf!)
                || mon.tipo == 'Jurídica' && ids.includes(mon.cnpj!)
                || mon.tipo == 'Jurídica' && ids.includes(mon.inscricaoEstadual!)){
                this.showMessage("Monitoradores já cadastrados no sistema foram descartados!", true)
              }

              else{
                monitoradores.push(mon)
                if(mon.tipo == 'Física') ids.push(mon.rg!, mon.cpf!)
                else ids.push(mon.cnpj!, mon.inscricaoEstadual!)
              }

            }
            else this.showMessage("Monitoradores com campos incorretos foram descartados!", true)

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
    formF.controls['nome'].setValidators([Validators.pattern('^[A-Za-zÀ-ÖØ-öø-ÿ ]*$'), Validators.maxLength(30), Validators.required])
    formF.controls['cpf'].setValidators([Validators.pattern('^[0-9]{11}$'), Validators.required])
    formF.controls['rg'].setValidators([Validators.pattern('^[0-9]{7}$'), Validators.required])
    formF.controls['dataNascimento'].setValidators([Validators.required])

    formF.controls['nome'].updateValueAndValidity()
    formF.controls['cpf'].updateValueAndValidity()
    formF.controls['rg'].updateValueAndValidity()
    formF.controls['dataNascimento'].updateValueAndValidity()
  }

  setFormJValidators(formJ: FormGroup): void{
    formJ.controls['razaoSocial'].setValidators([Validators.pattern('^[A-Za-zÀ-ÖØ-öø-ÿ ]*$'), Validators.maxLength(30), Validators.required])
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

  private static validateExcelMonitorador(mon: Monitorador): boolean {
    if(mon.tipo == 'Física') return /^[A-Za-zÀ-ÖØ-öø-ÿ ]{1,50}$/.test(mon.nome!) && /^\d{11}$/.test(mon.cpf!) && /^\d{7}$/.test(mon.rg!)
    else return /^[A-Za-zÀ-ÖØ-öø-ÿ ]{1,50}$/.test(mon.razaoSocial!) && /^\d{14}$/.test(mon.cnpj!) && /^\d{12}$/.test(mon.inscricaoEstadual!)
  }

  private static validataExcelEndereco(end: Enderecos): boolean {
    return /^\d{1,5}$/.test(end.numero!) && /^\d{8}$/.test(end.cep!) && /^.{13,14}$/.test(end.telefone!)
  }

  private static formatPhone(phone:number):string {
    return phone.toString().replace(/^(\d{0,2})(\d{0,9})$/, '($1) $2')
  }

}

