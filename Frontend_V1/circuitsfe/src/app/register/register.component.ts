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
  
  // Propiedades para validación de contraseña
  passwordStrength: number = 0;
  passwordFeedback: string = '';
  passwordErrors: string[] = [];
  
  constructor(private userService: UsersService, private router: Router) {}
  
  // Validar contraseña en tiempo real
  checkPasswordStrength() {
    this.passwordErrors = [];
    this.passwordStrength = 0;
    
    if (!this.password) {
      this.passwordFeedback = '';
      return;
    }
    
    // Verificar longitud mínima
    if (this.password.length < 8) {
      this.passwordErrors.push('La contraseña debe tener al menos 8 caracteres');
    } else {
      this.passwordStrength += 25;
    }
    
    // Verificar mayúsculas (no solo al inicio)
    if (!/[A-Z]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos una letra mayúscula');
    } else if (/^[A-Z]/.test(this.password)) {
      this.passwordErrors.push('La letra mayúscula no puede estar solo al inicio');
    } else {
      this.passwordStrength += 25;
    }

    // Verificar números
    if (!/[0-9]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos un número');
    } else {
      this.passwordStrength += 25;
    }

    // Verificar caracteres especiales (no solo al final)
    if (!/[\W_]/.test(this.password)) {
      this.passwordErrors.push('La contraseña debe contener al menos un carácter especial (#, -, _, @)');
    } else if (/[\W_]$/.test(this.password)) {
      this.passwordErrors.push('El carácter especial no puede estar solo al final');
    } else {
      this.passwordStrength += 25;
    }
    
    // Actualizar feedback
    if (this.passwordStrength <= 25) {
      this.passwordFeedback = 'Débil';
    } else if (this.passwordStrength <= 50) {
      this.passwordFeedback = 'Moderada';
    } else if (this.passwordStrength <= 75) {
      this.passwordFeedback = 'Buena';
    } else {
      this.passwordFeedback = 'Fuerte';
    }
  }
  
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
    
    // Verificar que la contraseña cumple con todos los requisitos
    if (this.passwordStrength < 100) {
      this.errorMessage = 'La contraseña no cumple con todos los requisitos de seguridad';
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