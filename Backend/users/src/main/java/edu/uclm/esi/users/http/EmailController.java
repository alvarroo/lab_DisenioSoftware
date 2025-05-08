package edu.uclm.esi.users.http;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import edu.uclm.esi.users.services.EmailService;


@RestController()
@RequestMapping("email")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/activate")
    public void activate(@RequestParam String token) {
        // Validar que el token no sea nulo o vacío para evitar errores
        if (token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El token no puede estar vacío");
        }

        emailService.activate(token);
    }

}
