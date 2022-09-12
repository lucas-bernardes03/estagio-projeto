import { Router } from '@angular/router';
import { EnderecoService } from './../endereco.service';
import { Monitorador, Enderecos } from './../monitorador.model';
import { MonitoradorService } from './../monitorador.service';
import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-monitorador-criar',
  templateUrl: './monitorador-criar.component.html',
  styleUrls: ['./monitorador-criar.component.css']
})
export class MonitoradorCriarComponent implements OnInit {

  formF!: FormGroup;
  formJ!: FormGroup;
  formComum!: FormGroup;
  formEndereco!: FormGroup

  hasNum = true

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
    ativo: null,
    enderecos: null
  }

  endereco: Enderecos = {
      id: null,
      endereco: null,
      numero: null,
      cep: null,
      bairro: null,
      telefone: null,
      cidade: null,
      estado: null,
      principal: null,
      monitoradorId: null
  }

  constructor(private service: MonitoradorService, 
    private formBuilder: FormBuilder, 
    private enderecoService: EnderecoService,
    private router:Router) { }

  ngOnInit(): void {
    this.instantiateForms()
    this.service.setFormComumValidators(this.formComum)
    this.enderecoService.setFormEnderecoValidators(this.formEndereco)
  }

  salvarMonitorador(): void {
    this.service.create(this.monitorador).subscribe(m => {
      this.enderecoService.adicionar(this.endereco).subscribe(e => {
        e.monitoradorId = m.id
        console.log(e)
        this.service.showMessage("Cadastro concluído com sucesso!")
        this.router.navigate(['monitoradores'])
      })
    })
  }

  cancelar(): void {
    this.router.navigate(['monitoradores'])
  }

  buscarCEP(): void{
    this.enderecoService.buscarCEP(this.endereco.cep!).subscribe(CEP => {
      this.endereco.endereco = CEP.logradouro
      this.endereco.bairro = CEP.bairro
      this.endereco.cidade = CEP.localidade
      this.endereco.estado = CEP.uf
    })
    
  }

  errorHandling = (control: string, error: string) => {
    if(this.formF.contains(control)) return this.formF.controls[control].hasError(error) && this.formF.controls[control].touched && this.formF.controls[control].dirty 
    else if (this.formJ.contains(control)) return this.formJ.controls[control].hasError(error) && this.formJ.controls[control].touched && this.formJ.controls[control].dirty 
    else if(this.formComum.contains(control)) return this.formComum.controls[control].hasError(error) && this.formComum.controls[control].touched && this.formComum.controls[control].dirty 
    return this.formEndereco.controls[control].hasError(error) && this.formEndereco.controls[control].touched && this.formEndereco.controls[control].dirty 
  }

  

  onChange($event: MatSelectChange){
    if($event.value === "Física"){
      this.formJ.reset()
      this.service.disableFormJValidators(this.formJ)
      this.service.setFormFValidators(this.formF)
    } 
    else{
      this.formF.reset()
      this.service.disableFormFValidators(this.formF)
      this.service.setFormJValidators(this.formJ)
    } 
    this.formComum.reset()
  }

  instantiateForms():void {
    this.formF = this.formBuilder.group({
      'nome': [null],
      'cpf': [null],
      'rg': [null],
      'dataNascimento': [null]
    })

    this.formJ = this.formBuilder.group({
      'razaoSocial': [null],
      'cnpj': [null],
      'inscricaoEstadual': [null]
    })

    this.formComum = this.formBuilder.group({
      'email': [null],
      'ativo': [null]
    })

    this.formEndereco = this.formBuilder.group({
      'endereco': [null],
      'numero': [null],
      'cep': [null],
      'bairro': [null],
      'telefone': [null],
      'cidade': [null],
      'estado': [null],
      'principal':[null]
    })
  }
}
