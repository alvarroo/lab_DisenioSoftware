package edu.uclm.esi.circuits.model;
import java.util.HashMap;
import java.util.UUID;


import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Circuit {
    
    @Id @Column(length = 36)
    private String id;
    private int outputQubits;

    @Transient
    @JsonProperty
    private int [][] table;
    @Column(length = 50)
    private String name;

    public Circuit(){
        this.id = UUID.randomUUID().toString();
    }

    public void setOutputQubits(int outputQubits){
       this.outputQubits = outputQubits;
    }

    public void setTable(HashMap<String, int[][]> table){
        this.table = table.get("table");
    }

    public int getOutputQubits() {
        return outputQubits;
    }
    public int[][] getTable() {
        return table;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String generateCode(String code){
        
        String circuit = generateCircuit();
        String measures = generateMeasures();
        String inputInit = generateInputInitialization();
        String outputInit = generateOutputInitialization();

        code = code.replace("#QUBITS#", table[0].length+"");
        code = code.replace("#OUTPUT QUBITS#", outputQubits+"");
        code = code.replace("#INPUT_INITIALIZATION#", inputInit);
        code = code.replace("#OUTPUT_INITIALIZATION#", outputInit);
        code = code.replace("#CIRCUIT#", circuit);
        code = code.replace("#MEASURES#", measures);
        
        return code;
    }

    // Genera la inicialización individual para los qubits de entrada
    private String generateInputInitialization() {
        StringBuilder sb = new StringBuilder();
        int inputQubits = table[0].length - outputQubits;
        
        for (int i = 0; i < inputQubits; i++) {
            sb.append("circuit.initialize(ZERO, ").append(i).append(")\n");
        }
        
        return sb.toString();
    }

    // Genera la inicialización individual para los qubits de salida
    private String generateOutputInitialization() {
        StringBuilder sb = new StringBuilder();
        int inputQubits = table[0].length - outputQubits;
        
        for (int i = inputQubits; i < table[0].length; i++) {
            sb.append("circuit.initialize(ZERO, ").append(i).append(")\n");
        }
        
        return sb.toString();
    }

    public String generateCircuit() {
   
        String inputQbits = recorrerYGuardar();
        StringBuilder res = new StringBuilder();
    
        // Añadir compuertas Hadamard para todos los qubits para ponerlos en superposición
        res.append("for i in range (0, qubits) :\n");
        res.append("    circuit.h(i)\n");
    
        for (int j = 0; j < table.length; j++) {
            for (int i = table[0].length - outputQubits; i < table[0].length; i++) {
                if (table[j][i] == 1) {
    
                    // Si es uno se cambian los valores de los qubits de control que son 0 a 1
                    for (int k = 0; k < table[0].length - outputQubits; k++) {
                        if (table[j][k] == 0) {
                            res.append("circuit.h(").append(k).append(")\n");
                            res.append("circuit.z(").append(k).append(")\n");
                            res.append("circuit.h(").append(k).append(")\n");
                        }
                    }
    
                    // Aplicar la transformación al qubit de salida
                    res.append("circuit.h(").append(i).append(")\n");
                    res.append("circuit.mcrz(pi, ").append(inputQbits).append(", ").append(i).append(")\n");
                    res.append("circuit.h(").append(i).append(")\n");
    
                    // Se deshacen los cambios en los qubits de control
                    for (int k = 0; k < table[0].length - outputQubits; k++) {
                        if (table[j][k] == 0) {
                            res.append("circuit.h(").append(k).append(")\n");
                            res.append("circuit.z(").append(k).append(")\n");
                            res.append("circuit.h(").append(k).append(")\n");
                        }
                    }
    
                    res.append("circuit.barrier()\n");
                }
            }
        }
    
        return res.toString();
    }

    public String generateMeasures() {
        StringBuilder res = new StringBuilder();
        
        // Utilizamos el formato de mediciones del ejemplo, midiendo todos los qubits
        res.append("for i in range(0, qubits) :\n");
        res.append("\tcircuit.measure(i, qubits-i-1)\n");
        
        return res.toString();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public int getCubits() {
        return table[0].length;
    }

    public String recorrerYGuardar() {
        StringBuilder sb = new StringBuilder();
        sb.append("["); // Inicia con corchete abierto

        for (int i = 0; i < table[0].length-outputQubits; i++) {
            sb.append(i); // Agrega el número
            if (i <  table[0].length-outputQubits-1) {
                sb.append(","); // Agrega coma si no es el último elemento
            }
        }

        sb.append("]"); // Cierra con corchete
        return sb.toString();
    }

}
