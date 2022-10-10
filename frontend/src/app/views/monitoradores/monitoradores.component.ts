import { MonitoradorService } from './../../components/monitorador/monitorador.service';
import { Router } from '@angular/router';
import { Component, OnInit} from '@angular/core';

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

  toXLSX(): void {
    this.monitoradorService.toXLSXAll()
  }

  toPDF(): void {
    
  }

}
