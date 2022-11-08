import { MonitoradorService } from './../../components/monitorador/monitorador.service';
import { Router } from '@angular/router';
import { Component, OnInit} from '@angular/core';
import { saveAs } from 'file-saver'

@Component({
  selector: 'app-monitoradores',
  templateUrl: './monitoradores.component.html',
  styleUrls: ['./monitoradores.component.css']
})
export class MonitoradoresComponent implements OnInit {

  constructor( private router: Router, private monitoradorService: MonitoradorService) { }

  ngOnInit(): void {
  }

  navigateToMonitoradorCriar(): void{
    this.router.navigate(['/monitoradores/criar'])
  }

  toXLS(): void {
    this.monitoradorService.downloadExcelAll().subscribe(blob => saveAs(blob, "Monitoradores.xls"))
  }

  uploadPage(): void {
    this.router.navigate(['/upload'])
  }

  toPDF(): void {
    this.monitoradorService.downloadPdf().subscribe(blob => saveAs(blob, 'MONITORADORES.pdf'));
  }

}
