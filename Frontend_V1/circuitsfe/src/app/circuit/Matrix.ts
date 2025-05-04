export class Matrix {
  table: number[][] = [];

  constructor(inputQubits: number, outputQubits: number) {
      const rows = Math.pow(2, inputQubits); // Número de filas
      const cols = inputQubits + outputQubits; // Número total de columnas

      this.table = Array.from({ length: rows }, (_, i) => {
          // Convertir el índice a binario y asignar a las primeras columnas
          const binaryRepresentation = i.toString(2).padStart(inputQubits, '0')
                                      .split('')
                                      .map(Number);
          return [...binaryRepresentation, ...Array(outputQubits).fill(0)];
      });
  }
}
