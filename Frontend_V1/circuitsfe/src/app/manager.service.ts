import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  token?: string;
  currentUser?: string;
  isAuthenticated: boolean = false;
  
  constructor() {
    // Intentar recuperar el token del sessionStorage al iniciar la aplicación
    const storedToken = sessionStorage.getItem('token');
    if (storedToken) {
      this.token = storedToken;
      this.isAuthenticated = true;
    }
  }
  
  logout() {
    this.token = undefined;
    this.currentUser = undefined;
    this.isAuthenticated = false;
    sessionStorage.removeItem('token');
  }
}
