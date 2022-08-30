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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
