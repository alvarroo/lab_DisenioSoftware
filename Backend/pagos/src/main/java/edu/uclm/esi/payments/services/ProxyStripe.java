package edu.uclm.esi.payments.services;


import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service 
public class ProxyStripe {

    public String prepay(JSONObject jsonConf) {

        long amount = (long)(jsonConf.getFloat("price")*100);
        String key = jsonConf.getString("Stripe.apiKey");
        Stripe.apiKey = key;
        String currency = jsonConf.getString("currency");
        //Se puede agregar el currency en el archivo de configuración


        PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
            .setCurrency(currency)
            .setAmount(amount)
            .build();

        try{
            PaymentIntent intent = PaymentIntent.create(params); 
            JSONObject jso = new JSONObject(intent.toJson()); 
            String clientSecret = jso.getString("client_secret"); 

            
            return clientSecret;
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Error al procesar tu solicitud", e);
        }
    }

}
