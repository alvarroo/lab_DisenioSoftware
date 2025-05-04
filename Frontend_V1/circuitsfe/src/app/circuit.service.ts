import { Injectable } from '@angular/core';
import { Matrix } from './circuit/Matrix';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CircuitService {

  constructor(private http : HttpClient) { }

  generateCode(outputQubits: number, matrix: Matrix, token?: string): Observable<string> {
    let body = {
      outputQubits: outputQubits,
      table: matrix
    }

    let headers = token ? { //Si token es distinto de null, tiene valor
      "token_generacion":  token
    }:undefined;

    return this.http.post<{code: string}>("http://localhost:8080/circuits/generateCode", body, {headers: headers})
      .pipe(
        map(response => response.code)
      );
  }
}
