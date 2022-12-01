import { Monitorador } from './monitorador/monitorador.model';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'appFilter' })
export class FilterPipe implements PipeTransform {
  
  transform(monitoradores: Monitorador[], searchText: string): any[] {
    if (!monitoradores) {
      return [];
    }
    if (!searchText) {
      return monitoradores;
    }
    
    searchText = searchText.toLocaleLowerCase();

    

    return monitoradores.filter(monitorador => {
      if(monitorador.tipo === 'Física'){
        return monitorador.nome!.toLocaleLowerCase().includes(searchText)
        || monitorador.cpf!.toLocaleLowerCase().includes(searchText)
        || monitorador.rg!.toLocaleLowerCase().includes(searchText)
        || monitorador.email!.toLocaleLowerCase().includes(searchText);
      }
      else{
        return monitorador.razaoSocial!.toLocaleLowerCase().includes(searchText)
        || monitorador.cnpj!.toLocaleLowerCase().includes(searchText)
        || monitorador.inscricaoEstadual!.toLocaleLowerCase().includes(searchText)
        || monitorador.email!.toLocaleLowerCase().includes(searchText);
      }
    });
  }
}