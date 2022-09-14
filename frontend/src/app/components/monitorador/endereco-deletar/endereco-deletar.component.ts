import { EnderecoService } from './../endereco.service';
import { DIALOG_DATA } from '@angular/cdk/dialog';
import { MatDialogRef } from '@angular/material/dialog';
import { Component, Inject, OnInit } from '@angular/core';

@Component({
  selector: 'app-endereco-deletar',
  templateUrl: './endereco-deletar.component.html',
  styleUrls: ['./endereco-deletar.component.css']
})
export class EnderecoDeletarComponent implements OnInit {

  constructor(@Inject(DIALOG_DATA) public data: number, private dialogRef: MatDialogRef<EnderecoDeletarComponent>, private service: EnderecoService) { }

  ngOnInit(): void {
  }

  deletarMonitorador(): void{
    this.service.deletar(this.data).subscribe(() => {
      this.service.showMessage("Endereço excluído com sucesso!")
      this.dialogRef.close(this.data)
    })
  }

  cancelar(): void{
    this.dialogRef.close()
  }
}
