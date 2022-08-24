import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-monitoradores',
  templateUrl: './monitoradores.component.html',
  styleUrls: ['./monitoradores.component.css']
})
export class MonitoradoresComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  navigateToMonitoradorCriar(): void{
    this.router.navigate(['/monitoradores/criar'])
  }

}
