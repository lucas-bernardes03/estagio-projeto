import { EnderecosComponent } from './components/monitorador/enderecos/enderecos.component';
import { MonitoradorCriarComponent } from './components/monitorador/monitorador-criar/monitorador-criar.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './views/home/home.component';
import { MonitoradoresComponent } from './views/monitoradores/monitoradores.component';

const routes: Routes = [
  {
    path: "",
    component: HomeComponent
  },
  {
    path: "monitoradores",
    component: MonitoradoresComponent
  },
  {
    path: "monitoradores/criar",
    component: MonitoradorCriarComponent
  },
  {
    path: "monitorador/:id/enderecos",
    component: EnderecosComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
