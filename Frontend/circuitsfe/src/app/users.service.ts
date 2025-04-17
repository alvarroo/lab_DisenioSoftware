import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private cliente: HttpClient) { }

  login(name: string, pwd: string) {
    let body = {
      username: name, 
      password: pwd  
    };
    return this.cliente.post<string>("http://localhost:8081/users/loginConBody", body, { responseType: 'text' as 'json' });
  }
}
