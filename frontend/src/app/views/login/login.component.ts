import { FormGroup, FormBuilder } from '@angular/forms';
import { AuthService } from './../../components/auth/auth.service';
import { MatDialogRef } from '@angular/material/dialog';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  test:boolean = true
  
  username: string = ''
  password: string = ''
  form!:FormGroup

  constructor(private dialogRef:MatDialogRef<LoginComponent>, private authService:AuthService, private formBuilder:FormBuilder) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      'username': [null],
      'password': [null]
    })
  }

  login():void {
    this.authService.login(this.username, this.password).subscribe(res => {
      if(res){
        this.dialogRef.close(res)
        this.authService.showMessage("Login feito com sucesso!")
      } 
      else this.authService.showMessage("Dados incorretos.", true)
    })
  }

  cancelar():void {
    this.dialogRef.close(false)
  }
}
