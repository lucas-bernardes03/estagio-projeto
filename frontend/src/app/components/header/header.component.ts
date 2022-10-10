import { delay } from 'rxjs';
import { LoginComponent } from './../../views/login/login.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  loginFlag: boolean = false

  constructor(private authService:AuthService, private dialog:MatDialog, private router:Router) { }

  ngOnInit(): void {
    let storeData = localStorage.getItem("isLogged");
    
    (storeData == 'true' ? this.loginFlag = true : this.loginFlag = false)
  }

  route(): void{
    if(this.loginFlag){
      this.authService.logout()
      this.ngOnInit()
      this.authService.showMessage("Logout feito com sucesso!")
      this.router.navigateByUrl('/monitoradores', { skipLocationChange: true }).then(() => {
        this.router.navigate(['/']);
      });
    }
    else{
      this.openDialogLogin()
    }
  }

  openDialogLogin():void {
    const dialogConfig = new MatDialogConfig()

    dialogConfig.disableClose = true
    dialogConfig.autoFocus = true

    const dialogRef = this.dialog.open(LoginComponent, dialogConfig)
    
    dialogRef.afterClosed().subscribe(result => {
      if(result){
        this.loginFlag = result
        this.ngOnInit()
        this.router.navigateByUrl('/monitoradores', { skipLocationChange: true }).then(() => {
          this.router.navigate(['/']);
        });
      } 
    })
  }

}
