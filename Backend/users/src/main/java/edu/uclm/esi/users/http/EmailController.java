package edu.uclm.esi.users.http;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import edu.uclm.esi.users.services.EmailService;


@RestController()
@RequestMapping("email")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/activate")
    public void activate(@RequestParam String token) {
        emailService.activate(token);
    }

}
