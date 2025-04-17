import { Component } from '@angular/core';
import { Matrix } from './Matrix';
import { CircuitService } from '../circuit.service'; 
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-circuit',
  standalone: false,
  templateUrl: './circuit.component.html',
  styleUrl: './circuit.component.css'
})
export class CircuitComponent {
    inputQubits : number; 
    outputQubits : number;
    matrix? : Matrix;

    constructor(private service: CircuitService, private manager: ManagerService) {
      this.inputQubits = 2;
      this.outputQubits = 2;
    }

    buildMatrix() {
      this.matrix = new Matrix(this.inputQubits, this.outputQubits);
    }

    negate(row: number,col: number) {
      this.matrix!.table[row][col] = this.matrix?.table[row][col] == 0 ? 1 : 0;
    }

    generateCode() {

      //let token = sessionStorage.getItem("token");
      let token = this.manager.token;
      /*if(!token){
        token = null;
        //token = "";
      }*/
      this.service.generateCode(this.outputQubits, this.matrix!, token).subscribe(
      //La admiración es para decirle a TypeScript que no es nulo, toma siempre valor
        ok => {
          console.log("Todo ha ido bien");
      }, 
      error => {
        console.log("Algo ha ido mal ");
      }
    )
    }
}

