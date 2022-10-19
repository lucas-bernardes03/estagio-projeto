import { Directive, EventEmitter, HostBinding, HostListener, Output } from '@angular/core';

@Directive({
  selector: '[appDragNDrop]'
})
export class DragNDropDirective {
  @HostBinding('class.fileover') fileOver: boolean = false;
  @Output() fileDropped = new EventEmitter<File>();

  @HostListener('dragover', ['$event']) onDragOver(evt: DragEvent){
    evt.preventDefault();
    evt.stopPropagation();
    this.fileOver = true;
  }

  @HostListener('dragleave', ['$event']) onDragLeave(evt: DragEvent){
    evt.preventDefault()
    evt.stopPropagation()
    this.fileOver = false
  }

  @HostListener('drop', ['$event']) public onDrop(evt: DragEvent){
    evt.preventDefault()
    evt.stopPropagation()
    this.fileOver = false

    let file = evt.dataTransfer?.files
    if(file) this.fileDropped.emit(file.item(0)!)
  }

  constructor() { }

}
