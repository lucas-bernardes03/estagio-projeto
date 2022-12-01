import { DIALOG_DATA } from '@angular/cdk/dialog';
import { MonitoradorService } from './../monitorador.service';
import { MatDialogRef } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';

@Component({
  selector: 'app-monitorador-deletar',
  templateUrl: './monitorador-deletar.component.html',
  styleUrls: ['./monitorador-deletar.component.css']
})
export class MonitoradorDeletarComponent implements OnInit {

  constructor(@Inject(DIALOG_DATA) public data: number, private service:MonitoradorService, private dialogRef: MatDialogRef<MonitoradorDeletarComponent>) { }

  ngOnInit(): void {
  }

  deletarMonitorador(): void{
    this.service.delete(this.data).subscribe(() =>{
      this.service.showMessage('Monitorador excluído com sucesso!')
      this.dialogRef.close(this.data)
    })
  }
  
  cancelar(): void{
    this.dialogRef.close()
  }
}
