import { MatSnackBar } from '@angular/material/snack-bar';
import { delay, Observable, of, tap } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  isLogged: boolean = false

  login(username: string, password: string): Observable<boolean>{
    this.isLogged = username == 'admin' && password == 'admin'
    localStorage.setItem('isLogged', this.isLogged ? 'true':'false')

    return of(this.isLogged).pipe(
      delay(666)
    )
  }

  logout(): void {
    this.isLogged = false
    localStorage.removeItem('isLogged')
  }

  showMessage(msg: string, error: boolean = false): void {
    this.snackbar.open(msg, "x", {
      duration: 3000,
      horizontalPosition: "right",
      verticalPosition: "top",
      panelClass: error ?  ["mat-cancel"]:["mat-success"]
    })
  }

  constructor(private snackbar:MatSnackBar) { }
}
