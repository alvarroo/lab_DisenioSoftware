import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentsService {
  private baseUrl = 'http://localhost:8082/payments';

  constructor(private http: HttpClient) { }
  
  prepay(): Observable<string> {
    return this.http.get<string>(`${this.baseUrl}/prepay`, { responseType: 'text' as 'json' });
  }
  
  confirmPayment(username: string): Observable<string> {
    return this.http.post<string>(`${this.baseUrl}/confirm`, { 
      username: username 
    }, { responseType: 'text' as 'json' });
  }
}
