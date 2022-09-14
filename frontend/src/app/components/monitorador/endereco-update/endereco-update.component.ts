import { EnderecoService } from './../endereco.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Enderecos } from './../monitorador.model';
import { DialogRef, DIALOG_DATA } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';

@Component({
  selector: 'app-endereco-update',
  templateUrl: './endereco-update.component.html',
  styleUrls: ['./endereco-update.component.css']
})
export class EnderecoUpdateComponent implements OnInit {

  form!: FormGroup;
  endereco = this.data

  constructor(@Inject(DIALOG_DATA) public data: Enderecos, private formBuilder: FormBuilder, private enderecoService:EnderecoService, private dialogRef: DialogRef) { }

  ngOnInit(): void {
    this.instatiateForm()
  }
  
  salvarEndereco():void {
    this.enderecoService.update(this.endereco).subscribe(() => {
      this.enderecoService.showMessage("Endereço atualizado com sucesso!")
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
    })
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
