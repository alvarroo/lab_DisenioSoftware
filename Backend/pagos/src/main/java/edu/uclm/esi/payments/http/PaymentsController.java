package edu.uclm.esi.payments.http;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
            throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT, "Error al procesar tu solicitud");
        }
    }
    
    @PostMapping("/confirm")
    public String confirmPayment(@RequestBody Map<String, String> body) {
        // Validar que el nombre de usuario no sea nulo o vacío
        if (body.get("username") == null || body.get("username").isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario es obligatorio");
        }

        try {
            String username = body.get("username");
            return this.service.confirmPayment(username);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al confirmar el pago: " + e.getMessage());
        }
    }
}
