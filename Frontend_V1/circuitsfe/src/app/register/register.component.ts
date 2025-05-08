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
    
    // Validar que los campos no estén vacíos
    if (!this.username || !this.email || !this.password || !this.confirmPassword) {
      this.errorMessage = 'Todos los campos son obligatorios';
      return;
    }

    // Validar que las contraseñas coincidan
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Las contraseñas no coinciden';
      return;
    }

    // Validar formato del email
    //^[^\s@]+: Asegura que el correo comience con uno o más caracteres que no sean espacios ni el símbolo @
    //@[^\s@]+: Asegura que haya un símbolo @ seguido de uno o más caracteres que no sean espacios
    //\.[^\s@]+$: Asegura que haya un punto (.) seguido de uno o más caracteres que no sean espacios, al final del correo
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.email)) {
      this.errorMessage = 'El formato del correo electrónico no es válido';
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