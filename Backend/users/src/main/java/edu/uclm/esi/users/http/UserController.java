package edu.uclm.esi.users.http;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @GetMapping("/loginConGetYParametros")
    public String login(@RequestParam String name, @RequestParam String pwd) {
        return userService.login(name, pwd);
    } 

    //PathVariable es una variable de la ruta entonces aparecería así /loginConPathYParametros/name
    @GetMapping("/loginConPathYParametros/{name}")
    public String login2(@PathVariable String name, @RequestParam String pwd) {
        return userService.login(name, pwd);
    }

    @PostMapping("/loginConPathYBody/{namew}")
    public String loginConPathYBody(@PathVariable String name, @RequestBody String pwd) {
        JSONObject json = new JSONObject(pwd);
        return userService.login(name, json.getString("pwd"));
    }

    @PostMapping("/updatePaymentStatus")
    public ResponseEntity<?> updatePaymentStatus(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        boolean hasPaid = (boolean) body.get("hasPaid");
        
        userService.updatePaymentStatus(username, hasPaid);
        return ResponseEntity.ok().build();
    }
}
