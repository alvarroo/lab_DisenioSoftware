package edu.uclm.esi.users.http;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController()
@RequestMapping("users")
//@CrossOrigin("*")
public class UserController {

    @GetMapping("/loginConGetYParametros")
    public String login(@RequestParam String name, @RequestParam String pwd){
        //Debe devolver un token
        return "1234";
        
    } 

    //PathVariable es una variable de la ruta entonces aparecería así /loginConPathYParametros/name
    @GetMapping("/loginConPathYParametros/{name}")
    public String login2(@PathVariable String name, @RequestParam String pwd){
        //Debe devolver un token
        return "1234";
        
    }

    @PostMapping("/loginConPathYBody/{namew}")
    public String loginConPathYBody(@PathVariable String name, @RequestBody String pwd){
        //Debe devolver un token
        return "1234";
        
    }

    @PostMapping("/loginConBody")
    public String loginConBody(@RequestBody Map<String, String> body){
        //Debe devolver un token
        return "1234";
        
    }

}
