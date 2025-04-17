import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { ManagerService } from './manager.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private manager: ManagerService, private router: Router) {}

  canActivate(): boolean {
    if (this.manager.token) {
      return true;
    } else {
      this.router.navigate(['/login']); // Redirige al login si no está autenticado
      return false;
    }
  }
}