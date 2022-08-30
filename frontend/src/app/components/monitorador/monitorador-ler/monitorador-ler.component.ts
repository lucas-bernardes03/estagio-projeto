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

  monitorador!: Monitorador
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

  openDialog(monitorador: Monitorador):void {
    const dialogConfig = new MatDialogConfig();
    console.log(monitorador)

    dialogConfig.disableClose = false
    dialogConfig.autoFocus = true
    dialogConfig.data = monitorador

    const dialogRef = this.dialog.open(MonitoradorUpdateComponent, dialogConfig)
  }

  findMon(id:number): void {
    //TODO
    try{
      console.log(id)
      this.monitoradorService.readById(id.toString()).subscribe(monitorador => {
        console.log(monitorador)
      })
      console.log(this.monitorador)
    }
    finally{
      this.openDialog(this.monitorador)
    }
  }
}
