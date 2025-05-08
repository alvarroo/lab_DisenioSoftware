package edu.uclm.esi.users.http;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.users.model.User;
import edu.uclm.esi.users.services.UserService;

@RestController()
@RequestMapping("users")
@CrossOrigin("*")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody Map<String, String> body) {
        // Validar que los campos no sean nulos o vacíos para evitar errores
        if (body.get("username") == null || body.get("username").isEmpty() ||
            body.get("password") == null || body.get("password").isEmpty() ||
            body.get("email") == null || body.get("email").isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los campos son obligatorios");
        }

        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        
        User user = userService.register(username, password, email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/loginConBody")
    public String loginConBody(@RequestBody Map<String, String> body) {
        // Validar que los campos no sean nulos o vacíos
        if (body.get("name") == null || body.get("name").isEmpty() ||
            body.get("pwd") == null || body.get("pwd").isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de usuario y contraseña son obligatorios");
        }

        String username = body.get("name");
        String password = body.get("pwd");
        
        return userService.login(username, password);
    }
    
    

    @PostMapping("/updatePaymentStatus")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        boolean hasPaid = (boolean) body.get("hasPaid");
        
        userService.updatePaymentStatus(username, hasPaid);
        return ResponseEntity.ok().build();
    }
}
