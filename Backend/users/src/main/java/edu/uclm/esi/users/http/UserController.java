package edu.uclm.esi.users.http;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");
        
        User user = userService.register(username, password, email);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/loginConBody")
    public String loginConBody(@RequestBody Map<String, String> body) {
        String username = body.get("name");
        String password = body.get("pwd");
        
        return userService.login(username, password);
    }
    
}
