import { Monitorador } from './../monitorador.model';
import { MonitoradorService } from './../monitorador.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-monitorador-ler',
  templateUrl: './monitorador-ler.component.html',
  styleUrls: ['./monitorador-ler.component.css']
})
export class MonitoradorLerComponent implements OnInit {

  monitoradores: Monitorador[] = []
  tableType = null
  
  monF: Monitorador[] = []
  monJ: Monitorador[] = []

  displayedColumnsF = ['id', 'nome', 'cpf', 'rg', 'dataNascimento', 'email', 'ativo', 'acoes']
  displayedColumnsJ = ['id', 'razaoSocial', 'cnpj', 'inscricaoEstadual', 'email', 'ativo', 'acoes']

  constructor(private monitoradorService: MonitoradorService) { }

  ngOnInit(): void {
    this.monitoradorService.read().subscribe(monitoradores => {
      this.monitoradores = monitoradores
      this.monF = monitoradores.filter(m => m.tipo === 'Física')
      this.monJ = monitoradores.filter(m => m.tipo === 'Jurídica')
    })
  }

}
