package edu.uclm.esi.payments.services;

import java.io.InputStream;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PaymentsService {

    @Autowired
    private ProxyStripe proxyStripe;
    
    @Autowired
    private ProxyUsers proxyUsers;

    public String prepay() throws Exception{
        JSONObject jsonConf = this.readConf("conf.json");
        return this.proxyStripe.prepay(jsonConf);
    }
    
    public String confirmPayment(String username) throws Exception {
        // Actualizamos el estado de pago del usuario
        proxyUsers.updatePaymentStatus(username, true);
        return "Pago confirmado correctamente";
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
