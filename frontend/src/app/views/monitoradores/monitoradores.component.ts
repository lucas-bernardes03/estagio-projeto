import { MonitoradorLerComponent } from './../../components/monitorador/monitorador-ler/monitorador-ler.component';
import { MonitoradorCriarComponent } from './../../components/monitorador/monitorador-criar/monitorador-criar.component';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';

@Component({
  selector: 'app-monitoradores',
  templateUrl: './monitoradores.component.html',
  styleUrls: ['./monitoradores.component.css']
})
export class MonitoradoresComponent implements OnInit {
  @ViewChild(MonitoradorLerComponent) monitoradorLer!: MonitoradorLerComponent

  constructor( private dialog: MatDialog) { }
  
  ngOnInit(): void {
  }

  navigateToMonitoradorCriar(): void{
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true
    dialogConfig.autoFocus = true

    const dialogRef = this.dialog.open(MonitoradorCriarComponent, dialogConfig)
    
    
    dialogRef.afterClosed().subscribe(result =>{
      console.log(result)
      if(result) this.monitoradorLer.ngOnInit()
    });
  }

}
