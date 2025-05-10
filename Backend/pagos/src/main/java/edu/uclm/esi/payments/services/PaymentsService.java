package edu.uclm.esi.payments.services;

import java.io.InputStream;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.payments.dao.UserRepository;
import edu.uclm.esi.payments.model.User;

@Service
public class PaymentsService {

    @Autowired
    private ProxyStripe proxyStripe;
    
     @Autowired
    private UserRepository userRepository;


    public String prepay() throws Exception{
        JSONObject jsonConf = this.readConf("conf.json");
        return this.proxyStripe.prepay(jsonConf);
    }
    
    public String confirmPayment(String token) throws Exception {
         User user = userRepository.findBySesionToken(token);
        
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        
        user.setHasPaid(true);
        userRepository.save(user);
        return "Pago confirmado";
    }
    

    private JSONObject readConf(String file) throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        try(InputStream fis = classLoader.getResourceAsStream(file)){
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            String conf = new String(buffer);
            return new JSONObject(conf);
        }
    }
}
