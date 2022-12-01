import { HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { MonitoradorDeletarComponent } from './../monitorador-deletar/monitorador-deletar.component';
import { MonitoradorUpdateComponent } from './../monitorador-update/monitorador-update.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Monitorador, PaginatedResponse } from './../monitorador.model';
import { MonitoradorService } from './../monitorador.service';
import { Component, OnInit } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';
import {saveAs} from "file-saver";

@Component({
  selector: 'app-monitorador-ler',
  templateUrl: './monitorador-ler.component.html',
  styleUrls: ['./monitorador-ler.component.css']
})
export class MonitoradorLerComponent implements OnInit {

  searchText: string = ''

  totalElementsF: number = 0
  monF: Monitorador[] = []

  totalElementsJ: number = 0
  monJ: Monitorador[] = []

  tableType = null

  displayedColumnsF = ['nome', 'cpf', 'rg', 'dataNascimento', 'email', 'ativo', 'acoes']
  displayedColumnsJ = ['razaoSocial', 'cnpj', 'inscricaoEstadual', 'email', 'ativo', 'acoes']

  constructor(private monitoradorService: MonitoradorService, private dialog:MatDialog, private router:Router) { }

  ngOnInit(): void {
    this.paginatedF(0,10, this.searchText)
    this.paginatedJ(0,10, this.searchText)
  }

  nextPage(event: PageEvent): void {
    if(this.tableType === "Física") this.paginatedF(event.pageIndex, event.pageSize, this.searchText)
    else this.paginatedJ(event.pageIndex, event.pageSize, this.searchText)
  }

  routeEnderecos(id: number): void {
    this.router.navigate([`/monitorador/${id}/enderecos`])
  }

  toXLS(id: number): void {
    this.monitoradorService.downloadExcelId(id).subscribe(blob => saveAs(blob, "monitoradorMUDAR.xls"))
  }

  openDialogDelete(id: number):void {
    const dialogConfig = new MatDialogConfig()

    dialogConfig.disableClose = true
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

  paginatedF(pageIndex:number, pageSize:number, search:string): void {
    const params = new HttpParams().set('tipo','Física').set('page',pageIndex).set('size',pageSize).set('search',search)

    this.monitoradorService.readPaginated(params).subscribe((data: PaginatedResponse) => {
      this.monF = data.content
      this.totalElementsF = data.totalElements
    })
  }

  paginatedJ(pageIndex:number, pageSize:number, search:string): void {
    const params = new HttpParams().set('tipo','Jurídica').set('page',pageIndex).set('size',pageSize).set('search',search)


    this.monitoradorService.readPaginated(params).subscribe((data:PaginatedResponse) => {
      this.monJ = data.content
      this.totalElementsJ = data.totalElements
    })
  }

  search(): void {
    if(this.tableType === "Física") this.paginatedF(0, 10, this.searchText)
    else this.paginatedJ(0, 10, this.searchText)
  }

}
