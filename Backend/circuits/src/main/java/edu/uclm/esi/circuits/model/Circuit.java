package edu.uclm.esi.circuits.model;
import java.util.HashMap;
import java.util.UUID;

import org.hibernate.Length;

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

    //implementar (es muy facil coges los que tengas los resultados a 1 le das la vuelta y una toffoli)
    public String generateCode(String code){
        
        String circuit = generateCircuit();
        String measures = generateMeasures();

        code = code.replace("#QUBITS#", table[0].length+"");

        code = code.replace("#OUTPUT QUBITS#", outputQubits+"");

        code = code.replace("#CIRCUIT#", circuit);
        
        code = code.replace("#MEASURES#", measures);
        
        return code;
        
    }

    public String generateCircuit() {
   
        String inputQbits = recorrerYGuardar();
        StringBuilder res = new StringBuilder();
    
        res.append(table[0].toString()).append("\n");
    
        for (int j = 0; j < table.length; j++) {
            for (int i = table[0].length - outputQubits; i < table[0].length; i++) {
                if (table[j][i] == 1) {
    
                    // Si es uno se cambian los valores de los qubits de control que son 0 a 1
                    for (int k = 0; k < table[0].length - outputQubits; k++) {
                        if (table[j][k] == 0) {
                            res.append("circuit.x(").append(k).append(")\n");
                        }
                    }
    
                    // Todos los qubits de control son 1 por lo que utilizamos la toffoli
                    res.append("circuit.mcx(").append(inputQbits).append(",").append(i).append(")\n");
    
                    // Se deshacen los cambios
                    for (int k = 0; k < table[0].length - outputQubits; k++) {
                        if (table[j][k] == 0) {
                            res.append("circuit.x(").append(k).append(")\n");
                        }
                    }
    
                    res.append("#------------Cambios deshechos------------\n");
                    res.append("ciruit.barrier()\n");
                }
            }
        }
    
        return res.toString();
    }

    public String generateMeasures() {
        StringBuilder res = new StringBuilder();
        for (int i = table[0].length - outputQubits; i < table[0].length; i++) {
            res.append("circuit.measure(").append(i).append(")\n");
        }

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
