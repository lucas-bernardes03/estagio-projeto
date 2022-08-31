import { MonitoradorDeletarComponent } from './../monitorador-deletar/monitorador-deletar.component';
import { MonitoradorUpdateComponent } from './../monitorador-update/monitorador-update.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Monitorador } from './../monitorador.model';
import { MonitoradorService } from './../monitorador.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-monitorador-ler',
  templateUrl: './monitorador-ler.component.html',
  styleUrls: ['./monitorador-ler.component.css']
})
export class MonitoradorLerComponent implements OnInit {

  monitorador: Monitorador = {
    id: null,
    tipo: null,  
    nome: null,
    cpf: null,
    rg: null,
    dataNascimento: null,
    razaoSocial: null,
    cnpj: null,
    inscricaoEstadual: null,
    email: null,
    ativo: null
  }

  monitoradores: Monitorador[] = []
  tableType = null
  
  monF: Monitorador[] = []
  monJ: Monitorador[] = []

  displayedColumnsF = ['id', 'nome', 'cpf', 'rg', 'dataNascimento', 'email', 'ativo', 'acoes']
  displayedColumnsJ = ['id', 'razaoSocial', 'cnpj', 'inscricaoEstadual', 'email', 'ativo', 'acoes']

  constructor(private monitoradorService: MonitoradorService, private dialog:MatDialog) { }

  ngOnInit(): void {
    this.monitoradorService.read().subscribe(monitoradores => {
      this.monitoradores = monitoradores
      this.monF = monitoradores.filter(m => m.tipo === 'Física')
      this.monJ = monitoradores.filter(m => m.tipo === 'Jurídica')
    })
  }

  openDialogDelete(id: number):void {
    const dialogConfig = new MatDialogConfig()

    dialogConfig.disableClose = false
    dialogConfig.autoFocus = true
    dialogConfig.data = id

    const dialogRef = this.dialog.open(MonitoradorDeletarComponent, dialogConfig)

    dialogRef.afterClosed().subscribe(result => {
      if(result) this.ngOnInit()
    })
  }

  openDialogUpdate(monitorador: Monitorador):void {
    const dialogConfig = new MatDialogConfig()

    dialogConfig.disableClose = true
    dialogConfig.autoFocus = true
    dialogConfig.data = monitorador

    const dialogRef = this.dialog.open(MonitoradorUpdateComponent, dialogConfig)

    dialogRef.afterClosed().subscribe(result => {
      if(result) this.ngOnInit()
    })
  }

  findMon(id:number):void{
     this.monitoradorService.readById(id.toString()).subscribe(monitorador => {
      this.openDialogUpdate(monitorador)
    })    
  }
  
 
}
