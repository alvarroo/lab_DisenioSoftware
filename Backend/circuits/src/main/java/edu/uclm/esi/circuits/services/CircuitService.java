package edu.uclm.esi.circuits.services;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.circuits.dao.CircuitDao;
import edu.uclm.esi.circuits.model.Circuit;

@Service
public class CircuitService {

    @Autowired //Hace que se instacie solo cuando se crea la clase servicio
    private CircuitDao circuitDao;


    public String generateCode(Circuit circuit, String token)throws Exception {
        
        if(circuit.getCubits() > 6){      
            if(token == null){
                throw new RuntimeException("El servicio solicitado requiere pago");
            }
           ProxyBEUsuarios.get().checkCredit(token);
        }

        String templateCode = this.readFile("ibm_local.txt");

        String code = circuit.generateCode(templateCode);

        if(circuit.getName() != null){
            circuitDao.save(circuit);
        }
        return code;
    }
        
    private String readFile(String fileName) throws Exception{
        
        ClassLoader classLoader = getClass().getClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(fileName)){

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            String s = new String(buffer);
            return s;
        }
    }
}
