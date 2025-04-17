package edu.uclm.esi.circuits.services;

import java.io.IOException;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;



public class ProxyBEUsuarios {

    private static ProxyBEUsuarios yo;
    private String urlUsuarios = "http://localhost:8081/tokens/";

    private ProxyBEUsuarios(){

    }

    public void checkCredit(String token) throws Exception{
        HttpGet httpGet = new HttpGet(urlUsuarios+"checkToken?token="+token);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {

                int code = response.getCode();
                if (code != 200){
                    throw new RuntimeException("El servicio solicitado requiere pago");
                }
            }
        }
    }

    public static ProxyBEUsuarios get(){
        if (yo == null){
            yo = new ProxyBEUsuarios();
        }
        return yo;
    }



}
