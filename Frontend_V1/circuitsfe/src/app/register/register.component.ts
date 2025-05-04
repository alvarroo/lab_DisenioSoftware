import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from '../users.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class RegisterComponent {
  username: string = '';
  password: string = '';
  confirmPassword: string = '';
  email: string = '';
  errorMessage: string = '';
  
  constructor(private userService: UsersService, private router: Router) {}
  
  register() {
    this.errorMessage = '';
    
    if (!this.username || !this.password || !this.email) {
      this.errorMessage = 'Por favor complete todos los campos obligatorios';
      return;
    }
    
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }
    
    this.userService.register(this.username, this.password, this.email).subscribe(
      () => {
        // Registro exitoso, redirigir a login
        this.router.navigate(['/login']);
      },
      error => {
        this.errorMessage = error.error && error.error.message 
          ? error.error.message 
          : 'Error al registrar el usuario. Inténtelo de nuevo más tarde.';
      }
    );
  }
  
  goToLogin() {
    this.router.navigate(['/login']);
  }
}