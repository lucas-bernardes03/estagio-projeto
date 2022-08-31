import { Monitorador } from './../monitorador.model';
import { Router } from '@angular/router';
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

  constructor(private service: MonitoradorService, private router: Router, private formBuilder: FormBuilder, private dialogRef:MatDialogRef<MonitoradorCriarComponent>) { }

  ngOnInit(): void {
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
    this.service.create(this.monitorador).subscribe(() => {
      this.service.showMessage("Cadastro concluído com sucesso!")
      this.dialogRef.close(this.monitorador)
    })
  }

  cancelar(): void {
    this.dialogRef.close()
  }

  errorHandling = (control: string, error: string) => {
    if(this.formF.contains(control)) return this.formF.controls[control].hasError(error) && this.formF.controls[control].touched 
    else if (this.formJ.contains(control)) return this.formJ.controls[control].hasError(error) && this.formJ.controls[control].touched 
    return this.formComum.controls[control].hasError(error) && this.formComum.controls[control].touched 
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


}
