import { Monitorador } from './../monitorador.model';
import { Router } from '@angular/router';
import { MonitoradorService } from './../monitorador.service';
import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';

@Component({
  selector: 'app-monitorador-criar',
  templateUrl: './monitorador-criar.component.html',
  styleUrls: ['./monitorador-criar.component.css']
})
export class MonitoradorCriarComponent implements OnInit {

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

  constructor(private service: MonitoradorService, private router: Router) { }

  ngOnInit(): void {
  }

  salvarMonitorador(): void {
    // this.service.create(this.monitorador).subscribe(() => {
    //   this.service.showMessage("Cadastro concluído com sucesso!")
    //   this.router.navigate(["/monitoradores"])
    // })
    console.log(this.monitorador)
  }

  cancelar(): void {
    this.router.navigate(["/monitoradores"])
  }

  onChange($event: MatSelectChange){
    if($event.value === "Física"){
      this.monitorador.razaoSocial = null
      this.monitorador.cnpj = null
      this.monitorador.inscricaoEstadual = null
    }
    else{
      this.monitorador.nome = null
      this.monitorador.cpf = null
      this.monitorador.rg = null
      this.monitorador.dataNascimento = null
    }
    this.monitorador.email = null
    this.monitorador.ativo = null
  }
}
