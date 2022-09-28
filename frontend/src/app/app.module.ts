import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HeaderComponent } from './components/header/header.component';

import { MatToolbarModule } from '@angular/material/toolbar';
import { FooterComponent } from './components/footer/footer.component';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MonitoradoresComponent } from './views/monitoradores/monitoradores.component';
import { MonitoradorCriarComponent } from './components/monitorador/monitorador-criar/monitorador-criar.component';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatRadioModule } from '@angular/material/radio';
import { MonitoradorLerComponent } from './components/monitorador/monitorador-ler/monitorador-ler.component';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { ReactiveFormsModule } from '@angular/forms';
import { MonitoradorUpdateComponent } from './components/monitorador/monitorador-update/monitorador-update.component';
import { MonitoradorDeletarComponent } from './components/monitorador/monitorador-deletar/monitorador-deletar.component';
import { FilterPipe } from './components/filter.pipe';
import { MatTooltipModule } from '@angular/material/tooltip';
import { EnderecosComponent } from './components/monitorador/enderecos/enderecos.component';
import { EnderecoDeletarComponent } from './components/monitorador/endereco-deletar/endereco-deletar.component';
import { EnderecoUpdateComponent } from './components/monitorador/endereco-update/endereco-update.component';
import { EnderecoCriarComponent } from './components/monitorador/endereco-criar/endereco-criar.component';
import { PhoneMaskDirective } from './components/monitorador/phone-mask.directive';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MonitoradoresComponent,
    MonitoradorCriarComponent,
    MonitoradorLerComponent,
    MonitoradorUpdateComponent,
    MonitoradorDeletarComponent,
    FilterPipe,
    EnderecosComponent,
    EnderecoDeletarComponent,
    EnderecoUpdateComponent,
    EnderecoCriarComponent,
    PhoneMaskDirective,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatCardModule,
    MatButtonModule,
    MatSnackBarModule,
    HttpClientModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatRadioModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    ReactiveFormsModule,
    MatTooltipModule
  ],
  providers: [MonitoradorLerComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
