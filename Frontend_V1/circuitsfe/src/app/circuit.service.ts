import { Injectable } from '@angular/core';
import { Matrix } from './circuit/Matrix';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {

  constructor(private http : HttpClient) { }

  generateCode(outputQubits: number, matrix: Matrix, token?: string, username?: string): Observable<string> {
    let body = {
      outputQubits: outputQubits,
      table: matrix
    }

    let headers: any = {};
    if (token) {
      headers["token_generacion"] = token;
    }
    if (username) {
      headers["username"] = username;
    }

    return this.http.post<{code: string}>("http://localhost:8080/circuits/generateCode", body, {headers: headers})
      .pipe(
        map(response => response.code)
      );
  }

  getUserCircuits(username: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/circuits/userCircuits/${username}`);
  }

  getCircuitById(id: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/circuits/circuit/${id}`);
  }
}
