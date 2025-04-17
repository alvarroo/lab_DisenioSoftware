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

    @Id
    @Column(length = 36)
    private String id;
    private int outputQubits;

    @Transient
    @JsonProperty
    private int[][] table;
    @Column(length = 50)
    private String name;

    public Circuit() {
        this.id = UUID.randomUUID().toString();
    }

    public void setOutputQubits(int outputQubits) {
        this.outputQubits = outputQubits;
    }

    public void setTable(HashMap<String, int[][]> table) {
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

    public String generateCode(String code) {
        code = code.replace("#QUBITS", "" + this.table[0].length);
        code = code.replace("#OUTPUT QUBITS", "" + this.outputQubits);
        code = code.replace("#CIRCUIT#", generateCircuit());
        //Otros reemplazos
        return code;
    }

    // implementar (es muy facil coges los que tengas los resultados a 1 le das la
    // vuelta y una toffoli)
    // En verdad esto es solo la parte de #CIRCUIT#
    public String generateCircuit() {
        String inputQbits = recorrerYGuardar();
        String res = "";
        res += table[0].toString() + "\n";
        for (int j = 0; j < table.length; j++) {
            for (int i = table[0].length - outputQubits; i < table[0].length; i++) {
                if (table[j][i] == 1) {

                    // Si es uno se cambian los valores de los qubits de control que son 0 a 1
                    for (int k = 0; k < table[0].length - outputQubits; k++) {
                        if (table[j][k] == 0) {
                            res += "circuit.x( " + k + ")\n";
                        }
                    }
                    // Todos los qubits de control son 1 por lo que utilizamos la toffoli
                    res += "circuit.mcx( " + inputQbits + "," + i + " )\n";

                    // Se deshacen los cambios
                    for (int k = 0; k < table[0].length - outputQubits; k++) {
                        if (table[j][k] == 0) {
                            res += "circuit.x( " + k + ")\n";
                        }
                    }

                    res += "#------------Cambios desehchos------------\n";

                }
            }
        }

        /*
         *
         * Este método es muy facil, hay que diferenciar las salidas, y coger solo las
         * que sean uno, cuando sean uno simplemente lo que hay
         * que hacer es mirar las combinaciones y ponerle las x a las que sean 0 y nada
         * a las que sean 1 y se cogen las de control y se deshacen
         * los cambios.
         * 
         */

        // Hay que empezar con una plantilla y tener cuidado si hay que tener qubits
        // auxiliares.

        return res;
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

        for (int i = 0; i < table[0].length - outputQubits; i++) {
            sb.append(i); // Agrega el número
            if (i < table[0].length - outputQubits - 1) {
                sb.append(","); // Agrega coma si no es el último elemento
            }
        }

        sb.append("]"); // Cierra con corchete
        return sb.toString();
    }

}
