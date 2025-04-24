import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private baseUrl = 'http://localhost:8081/users';

  constructor(private http: HttpClient) { }

  register(username: string, password: string, email: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, {
      username,
      password,
      email
    });
  }

  login(username: string, password: string): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/loginConBody`, {
      name: username,
      pwd: password
    }, { responseType: 'text' as 'json' });
  }
  
  getUserData(token: string): Observable<any> {
    return this.http.get(`http://localhost:8081/tokens/getUserInfo`, {
      params: { token }
    });
  }
}
