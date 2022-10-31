import { DIALOG_DATA } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { EnderecoService } from '../endereco.service';
import { Enderecos } from '../monitorador.model';

@Component({
  selector: 'app-endereco-criar',
  templateUrl: './endereco-criar.component.html',
  styleUrls: ['./endereco-criar.component.css']
})
export class EnderecoCriarComponent implements OnInit {

  form!: FormGroup;

  numeroPlaceholder = 'Número'
  numero = true

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

  constructor(@Inject(DIALOG_DATA) public data: number, private formBuilder: FormBuilder, private enderecoService:EnderecoService, private dialogRef: MatDialogRef<EnderecoCriarComponent>) { }

  ngOnInit(): void {
    this.instatiateForm()
  }

  salvarEndereco():void {
    this.enderecoService.adicionar(this.data, this.endereco).subscribe(() => {
      this.enderecoService.showMessage("Novo endereço registrado!")
      this.dialogRef.close(this.endereco)
    })
  }

  cancelar(): void {
    this.dialogRef.close()
  }

  buscarCEP(): void {
    this.enderecoService.buscarCEP(this.endereco.cep!).subscribe(CEP => {
      this.endereco.endereco = CEP.logradouro
      this.endereco.bairro = CEP.bairro
      this.endereco.cidade = CEP.localidade
      this.endereco.estado = CEP.uf

      if(CEP.localidade) this.disableCEPInputs()
    })
  }

  disableCEPInputs():void {
    this.form.controls['cidade'].disable()
    this.form.controls['estado'].disable()
  }

  disableNumero():void {
    this.numero = !this.numero
    if(!this.numero){
      this.form.controls['numero'].setValue(null)
      this.form.controls['numero'].disable()
      this.numeroPlaceholder = 'Sem Número'
    }
    else{
      this.form.controls['numero'].enable()
      this.numeroPlaceholder = 'Número'
    }
  }

  instatiateForm():void {
    this.form = this.formBuilder.group({
      'endereco': [null],
      'numero': [null],
      'cep': [null],
      'bairro': [null],
      'telefone': [null],
      'cidade': [null],
      'estado': [null],
      'principal':[null]
    })

    this.enderecoService.setFormEnderecoValidators(this.form)
  }

  errorHandling = (control: string, error: string) => {
    return this.form.controls[control].hasError(error) && this.form.controls[control].touched && this.form.controls[control].dirty
  }
}
