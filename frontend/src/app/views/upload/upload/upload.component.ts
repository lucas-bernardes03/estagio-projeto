import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {

  file: any

  constructor() { }

  ngOnInit(): void {
  }

  onDrop(file: File): void {
    console.log('file: ',file)
    
    let fileReader = new FileReader()
    fileReader.onload = (e) => {
      console.log(fileReader.result)
    }
    fileReader.readAsText(file)
  }
}
