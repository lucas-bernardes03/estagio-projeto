import { UploadComponent } from './views/upload/upload/upload.component';
import { EnderecosComponent } from './components/monitorador/enderecos/enderecos.component';
import { MonitoradorCriarComponent } from './components/monitorador/monitorador-criar/monitorador-criar.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MonitoradoresComponent } from './views/monitoradores/monitoradores.component';
import {LoginComponent} from "./views/login/login.component";


const routes: Routes = [
  {
    path: "",
    component: MonitoradoresComponent
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
  },
  {
    path: "upload",
    component: UploadComponent
  },
  {
    path: "login",
    component: LoginComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
