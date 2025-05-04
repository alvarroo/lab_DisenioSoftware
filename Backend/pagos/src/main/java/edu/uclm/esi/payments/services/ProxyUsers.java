package edu.uclm.esi.payments.services;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ProxyUsers {
    
    private static final String USER_SERVICE_URL = "http://localhost:8081/users/updatePaymentStatus";
    
    public void updatePaymentStatus(String username, boolean hasPaid) throws Exception {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("username", username);
        jsonRequest.put("hasPaid", hasPaid);
        
        StringEntity entity = new StringEntity(jsonRequest.toString(), ContentType.APPLICATION_JSON);
        
        HttpPost httpPost = new HttpPost(USER_SERVICE_URL);
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type", "application/json");
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                if (statusCode != 200) {
                    throw new RuntimeException("Error al actualizar el estado de pago del usuario: " + statusCode);
                }
            }
        }
    }
}