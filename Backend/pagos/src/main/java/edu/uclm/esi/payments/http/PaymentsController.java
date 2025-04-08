package edu.uclm.esi.payments.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.payments.services.PaymentsService;


@RestController()
@RequestMapping("payments")
@CrossOrigin("*")
public class PaymentsController {
    
    @Autowired //Se arranca al iniciar la aplicación
    private PaymentsService service;


    @GetMapping("/prepay")
    public String prepay(){
        try{
            return this.service.prepay();
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT,"Error al procesar tu solicitud");
        }
    } 


}
