package edu.uclm.esi.circuits.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.circuits.model.*;
import edu.uclm.esi.circuits.services.CircuitService;
import jakarta.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("circuits")
@CrossOrigin("*")
public class CircuitController {

    @Autowired
    private CircuitService service;

    //http.../../generateCode
    @PostMapping("/generateCode")
    public Map<String, String> generateCode(HttpServletRequest request, 
                                @RequestParam(required=false) String name, @RequestBody Circuit circuit){
      
        if (name != null){
            circuit.setId(name);
        }
        String token = request.getHeader("token_generacion");
        try{
            String code = this.service.generateCode(circuit, token);
            Map<String, String> response = new HashMap<>();
            response.put("code", code);
            return response;
        }  catch (Exception e){
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, e.getMessage());
        }
    }
}
