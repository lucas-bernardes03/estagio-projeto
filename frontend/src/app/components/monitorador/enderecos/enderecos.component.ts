import { EnderecoCriarComponent } from './../endereco-criar/endereco-criar.component';
import { EnderecoUpdateComponent } from './../endereco-update/endereco-update.component';
import { EnderecoDeletarComponent } from './../endereco-deletar/endereco-deletar.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MonitoradorService } from './../monitorador.service';
import { EnderecoService } from './../endereco.service';
import { Enderecos } from './../monitorador.model';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-enderecos',
  templateUrl: './enderecos.component.html',
  styleUrls: ['./enderecos.component.css']
})
export class EnderecosComponent implements OnInit {
  monId: string = ''
  searchText: string = ''
  
  enderecos: Enderecos[] = []
  displayedColumns = ['id','endereco','numero','cep','bairro','telefone','cidade','estado','principal','acoes']


  constructor(private monitoradorService:MonitoradorService, private router:Router, private dialog: MatDialog, private enderecoService:EnderecoService) { }

  ngOnInit(): void {
    this.monId = this.router.url.replace(/\D/g, "")
    this.monitoradorService.retrieveEnderecos(Number.parseInt(this.monId)).subscribe(enderecos => {
      this.enderecos = enderecos
    })  
  }

  voltar(): void{
    this.router.navigate(['/monitoradores'])
  }

  novoEnderecoDialog(): void {
    const dialogConfig = new MatDialogConfig()
    
    dialogConfig.disableClose = false
    dialogConfig.autoFocus = true
    dialogConfig.data = this.monId

    const dialogRef = this.dialog.open(EnderecoCriarComponent, dialogConfig)

    dialogRef.afterClosed().subscribe(result => {
      if(result) this.ngOnInit()
    })
  }

  editEndereco(endereco: Enderecos):void {
    const dialogConfig = new MatDialogConfig()
    
    dialogConfig.disableClose = false
    dialogConfig.autoFocus = true
    dialogConfig.data = endereco

    const dialogRef = this.dialog.open(EnderecoUpdateComponent, dialogConfig)

    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.ngOnInit()
        console.log("salvou")
      } 
    })
  }
  
  findEnd(id:number): void{
    this.enderecoService.readById(id).subscribe(endereco => {
      this.editEndereco(endereco)
    })
  }

  openDialogDelete(id:number):void {
    const dialogConfig = new MatDialogConfig()
    
    dialogConfig.disableClose = true
    dialogConfig.autoFocus = true
    dialogConfig.data = id

    const dialogRef = this.dialog.open(EnderecoDeletarComponent, dialogConfig)

    dialogRef.afterClosed().subscribe(result => {
      if(result) this.ngOnInit()
    })
  }

}
