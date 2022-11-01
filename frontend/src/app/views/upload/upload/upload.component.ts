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
  displayedColumns = ['tipo', 'nomeRazao', 'cpfCnpj', 'rgInscricao', 'email', 'endereco', 'telefone']


  constructor(private monitoradorService: MonitoradorService, private router:Router) { }

  ngOnInit(): void {
  }

  onDrop(file: File): void {
    this.monitoradorService.readExcel(file).subscribe(m => {
      this.monitoradores = m
    })
  }

  fileBrowser($event: any): void {
    this.monitoradorService.readExcel($event.files[0]).subscribe(m => {
      this.monitoradores = m
    })
  }

  salvar(): void {
    this.monitoradorService.createMany(this.monitoradores).subscribe(m => {
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
}
