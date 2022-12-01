import { MonitoradorLerComponent } from './../monitorador-ler/monitorador-ler.component';
import { Router } from '@angular/router';
import { EnderecoService } from './../endereco.service';
import { Monitorador, Enderecos } from './../monitorador.model';
import { MonitoradorService } from './../monitorador.service';
import { Component, OnInit } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-monitorador-criar',
  templateUrl: './monitorador-criar.component.html',
  styleUrls: ['./monitorador-criar.component.css']
})
export class MonitoradorCriarComponent implements OnInit {

  formF!: FormGroup;
  formJ!: FormGroup;
  formComum!: FormGroup;
  formEndereco!: FormGroup;

  numeroPlaceholder = 'Número'
  numero = true

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
    enderecos: []
  }

  endereco: Enderecos = {
      id: null,
      endereco: null,
      numero: null,
      cep: null,
      bairro: null,
      telefone: '',
      cidade: null,
      estado: null,
      principal: null
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
    this.monitorador.enderecos?.push(this.endereco)
    this.service.create(this.monitorador).subscribe(m => {
      if(m.id == null){
        if(m.tipo == 'Física') m.cpf == null ? this.service.showMessage('CPF já cadastrado no sistema.', true) : this.service.showMessage('RG já cadastrado no sistema.', true)
        else m.cnpj == null ? this.service.showMessage('CNPJ já cadastrado no sistema.', true) : this.service.showMessage('Inscrição Estadual já cadastrada no sistema.', true)
      }
      else{
        this.service.showMessage('Monitorador cadastrado com sucesso!')
        this.router.navigate(['monitoradores'])
      }
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

      if(CEP.localidade) this.disableCEPInputs()
    })
  }

  disableCEPInputs():void {
    this.formEndereco.controls['cidade'].disable()
    this.formEndereco.controls['estado'].disable()
  }

  disableNumero():void {
    this.numero = !this.numero
    if(!this.numero){
      this.formEndereco.controls['numero'].reset()
      this.formEndereco.controls['numero'].disable()
      this.numeroPlaceholder = 'Sem Número'
    }
    else{
      this.formEndereco.controls['numero'].enable()
      this.numeroPlaceholder = 'Número'
    }
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
