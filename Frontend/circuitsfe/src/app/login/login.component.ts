import { Component } from '@angular/core';
import { UsersService } from '../users.service';
import { ManagerService } from '../manager.service';

//Nuevo
import { Router } from '@angular/router';



@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  name?: string;
  pwd?: string;

  /*
  constructor(private usersService: UsersService, private manager: ManagerService) {
  }
  
 

  login(){
    if(!this.name || !this.pwd){
      return;
    }
    this.usersService.login(this.name!, this.pwd!).subscribe(
      (token)=>{
        this.manager.token = token;
      },
      (error)=>{
        console.log(error);
      }
    );
  }
    */

  //Para hacer que login aparezca en una ventana aparte
constructor(
  private usersService: UsersService,
  private manager: ManagerService,
  private router: Router // Inyecta el Router en el constructor
) {}

login() {
  if (!this.name || !this.pwd) {
    return;
  }
  this.usersService.login(this.name!, this.pwd!).subscribe(
    (token) => {
      console.log('Token recibido:', token); // Verifica el token recibido
      this.manager.token = token;
      this.router.navigate(['/circuit']);
    },
    (error) => {
      console.log('Error al iniciar sesión:', error);
    }
  );
}
}
