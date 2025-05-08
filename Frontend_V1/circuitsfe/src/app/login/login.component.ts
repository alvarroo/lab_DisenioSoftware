import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from '../users.service';
import { ManagerService } from '../manager.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  honeypot: string = ''; // Campo honeypot para detectar bots

  constructor(
    private usersService: UsersService, 
    private manager: ManagerService,
    private router: Router
  ) {}

  login() {
    this.errorMessage = '';

    if (this.honeypot) {
      this.errorMessage = 'Solicitud rechazada: posible bot detectado.';
      return;
    }

    if (!this.username || !this.password) {
      this.errorMessage = 'Por favor ingrese nombre de usuario y contraseña';
      return;
    }

    this.usersService.login(this.username, this.password).subscribe(
      (token) => {
        this.manager.token = token;
        sessionStorage.setItem('token', token);
        this.manager.currentUser = this.username;
        this.manager.isAuthenticated = true; // Asegurar que isAuthenticated sea true
        // Redirección a la página principal
        this.router.navigate(['/home']);
      },
      (error) => {
        if (error.status === 401) {
          this.errorMessage = 'Credenciales incorrectas. Por favor inténtelo de nuevo.';
        } else if (error.status === 403) {
          this.errorMessage = 'El usuario no ha sido activado. Por favor revise su correo electrónico.';
        } else {
          this.errorMessage = 'Ha ocurrido un error inesperado. Por favor inténtelo más tarde.';
        }
      }
    );
  }
  
  goToRegister() {
    this.router.navigate(['/register']);
  }
}
