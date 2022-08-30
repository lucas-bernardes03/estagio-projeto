import { Monitorador } from './../monitorador.model';
import { MonitoradorService } from './../monitorador.service';
import { DIALOG_DATA } from '@angular/cdk/dialog';
import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-monitorador-update',
  templateUrl: './monitorador-update.component.html',
  styleUrls: ['./monitorador-update.component.css']
})
export class MonitoradorUpdateComponent implements OnInit {

  formF!: FormGroup;
  formJ!: FormGroup;
  formComum!: FormGroup;

  monitorador = this.data

  constructor(@Inject(DIALOG_DATA) public data: Monitorador, private service: MonitoradorService, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    // this.service.readById(this.data.toString()).subscribe(monitorador =>{
    //   this.monitorador = monitorador
    // })


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
      'email': [null,  [Validators.required, Validators.email]],
      'ativo': [null, Validators.required]
    })

  }

  salvarMonitorador(): void {
    console.log(this.data)
  }

  cancelar(): void {
    console.log(this.data)
  }

  errorHandling = (control: string, error: string) => {
    if(this.formF.contains(control)) return this.formF.controls[control].hasError(error) && this.formF.controls[control].touched 
    else if (this.formJ.contains(control)) return this.formJ.controls[control].hasError(error) && this.formJ.controls[control].touched 
    return this.formComum.controls[control].hasError(error) && this.formComum.controls[control].touched 
  }

  setFormFValidators(): void{
    this.formF.controls['nome'].setValidators([Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(30), Validators.required])
    this.formF.controls['cpf'].setValidators([Validators.pattern('^[0-9]{11}$'), Validators.required])
    this.formF.controls['rg'].setValidators([Validators.pattern('^[0-9]{7}$'), Validators.required])
    this.formF.controls['dataNascimento'].setValidators([Validators.required])
    
    this.formF.controls['nome'].updateValueAndValidity()
    this.formF.controls['cpf'].updateValueAndValidity()
    this.formF.controls['rg'].updateValueAndValidity()
    this.formF.controls['dataNascimento'].updateValueAndValidity()
  }

  setFormJValidators(): void{
    this.formJ.controls['razaoSocial'].setValidators([Validators.pattern('^[a-zA-Z ]*$'), Validators.maxLength(30), Validators.required])
    this.formJ.controls['cnpj'].setValidators([Validators.pattern('^[0-9]{14}$'), Validators.required])
    this.formJ.controls['inscricaoEstadual'].setValidators([Validators.pattern('^[0-9]{9}$'), Validators.required])

    this.formJ.controls['razaoSocial'].updateValueAndValidity()
    this.formJ.controls['cnpj'].updateValueAndValidity()
    this.formJ.controls['inscricaoEstadual'].updateValueAndValidity()
  }

  disableFormFValidators():void{
    this.formF.controls['nome'].clearValidators()
    this.formF.controls['cpf'].clearValidators()
    this.formF.controls['rg'].clearValidators()
    this.formF.controls['dataNascimento'].clearValidators()

    this.formF.controls['nome'].updateValueAndValidity()
    this.formF.controls['cpf'].updateValueAndValidity()
    this.formF.controls['rg'].updateValueAndValidity()
    this.formF.controls['dataNascimento'].updateValueAndValidity()
  }

  disableFormJValidators():void{
    this.formJ.controls['razaoSocial'].clearValidators()
    this.formJ.controls['cnpj'].clearValidators()
    this.formJ.controls['inscricaoEstadual'].clearValidators()

    this.formJ.controls['razaoSocial'].updateValueAndValidity()
    this.formJ.controls['cnpj'].updateValueAndValidity()
    this.formJ.controls['inscricaoEstadual'].updateValueAndValidity()
  }

}
