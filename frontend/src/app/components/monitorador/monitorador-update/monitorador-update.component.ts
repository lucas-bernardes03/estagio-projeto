import { MatDialogRef } from '@angular/material/dialog';
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

  constructor(@Inject(DIALOG_DATA) public data: Monitorador, private service: MonitoradorService, private formBuilder: FormBuilder, private dialogRef: MatDialogRef<MonitoradorUpdateComponent>) { }

  ngOnInit(): void {
    this.instantiateForms()

    if(this.monitorador.tipo === 'Física'){
      this.service.setFormFValidators(this.formF)
      this.service.disableFormJValidators(this.formJ)
    } 
    else{
      this.service.setFormJValidators(this.formJ)
      this.service.disableFormFValidators(this.formF)
    }
    
  }

  salvarMonitorador(): void {
    this.service.checkIguaisUpdate(this.monitorador, this.monitorador.id!).subscribe(check => {
      if(!check){
        this.service.update(this.monitorador).subscribe(() =>{
          this.service.showMessage('Monitorador atualizado com sucesso!')
          this.dialogRef.close(this.monitorador)
        })
      }
      else{
        if(this.monitorador.tipo === "Física") this.service.showMessage("CPF/RG já cadastrado no sistema.", true)
        else this.service.showMessage("CNPJ/Inscrição Estadual já cadastrado no sistema.", true)
      }
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
      'email': [null,  [Validators.required, Validators.email]],
      'ativo': [null, Validators.required]
    })
  }
}
