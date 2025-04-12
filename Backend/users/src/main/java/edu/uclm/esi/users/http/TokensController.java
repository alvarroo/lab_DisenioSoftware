package edu.uclm.esi.users.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.users.model.User;
import edu.uclm.esi.users.services.TokenService;

@RestController()
@RequestMapping("tokens")
@CrossOrigin("*")
public class TokensController {
    
    @Autowired
    private TokenService tokenService;

    @GetMapping("/checkToken")
    public void checkToken(@RequestParam String token){
        if (!tokenService.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Token inválido o usuario sin pago");
        }
    }
    
    @GetMapping("/getUserInfo")
    public User getUserInfo(@RequestParam String token) {
        return tokenService.getUserFromToken(token);
    }
}
