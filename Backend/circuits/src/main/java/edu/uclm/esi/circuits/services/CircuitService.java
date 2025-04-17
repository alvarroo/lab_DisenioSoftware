package edu.uclm.esi.circuits.services;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.circuits.dao.CircuitDao;
import edu.uclm.esi.circuits.model.Circuit;

@Service
public class CircuitService {

    @Autowired // Hace que se instacie solo cuando se crea la clase servicio
    private CircuitDao circuitDao;

    // Lo tiene asi: public Map<String, Object> generateCode(Circuit circuit, String
    // token)throws Exception{ {

    public String generateCode(Circuit circuit, String token) throws Exception {
        {

            if (circuit.getCubits() > 6) {
                if (token == null) {
                    throw new RuntimeException("El servicio solicitado requiere pago");
                }
                ProxyBEUsuarios.get().checkCredit(token);
            }

            String templateCode = this.readFile("ibm.local.txt");

            String code = circuit.generateCode(templateCode);
            if (circuit.getName() != null) {
                circuitDao.save(circuit);
            }
            return code;
        }
    }

    private String readFile(String fileName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream fis = classLoader.getResourceAsStream(fileName)) {
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String s = new String(b);
            return s;
        }
    }

}
