import { Router } from '@angular/router';
import { MonitoradorService } from './../../../components/monitorador/monitorador.service';
import { Component, OnInit } from '@angular/core';
import { Monitorador } from 'src/app/components/monitorador/monitorador.model';
import {saveAs} from "file-saver";

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  monitoradores: Monitorador[] = []
  monitoradoresVal: Monitorador[] = []
  displayedColumns = ['tipo', 'nomeRazao', 'cpfCnpj', 'rgInscricao', 'email', 'endereco', 'telefone']

  constructor(private monitoradorService: MonitoradorService, private router:Router) { }

  ngOnInit(): void {
  }

  onDrop(file: File): void {
    this.readFile(file)
  }

  fileBrowser($event: any): void {
    this.readFile($event.files[0])
  }

  salvar(): void {
    this.monitoradorService.createMany(this.monitoradoresVal).subscribe(m => {
      this.monitoradorService.showMessage("Monitoradores cadastrados com sucesso!")
      this.router.navigate(['/'])
    })
  }


  voltar(): void {
    this.router.navigate(['/'])
  }

  excelModelo():void{
    this.monitoradorService.downloadModeloExcel().subscribe(blob => saveAs(blob, 'PlanilhaModelo.xlsx'))
  }

  private readFile(file: File): void {
    this.monitoradorService.readExcel(file).subscribe(m => {
      m.forEach(mon =>{
        this.monitoradores.push(mon)

        if(!this.cadastroVal(mon) || !this.registroVal(mon) || !this.nomeVal(mon)) this.monitoradorService.showMessage("Monitoradores com dados incorretos não serão adicionados!", true)
        else this.monitoradoresVal.push(mon)
      })
    })
  }

  cadastroVal(m: Monitorador): boolean{
    return this.monitoradorService.validateCadastro(m)
  }

  registroVal(m: Monitorador): boolean{
    return this.monitoradorService.validateRegistro(m)
  }

  nomeVal(m: Monitorador): boolean{
    return this.monitoradorService.validateNome(m)
  }
}
