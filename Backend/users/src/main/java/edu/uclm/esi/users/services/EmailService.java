package edu.uclm.esi.users.services;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    public void sendConfirmationEmail(String email, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Confirmación de Registro");
        confirmationLink = confirmationLink + generateConfirmationToken();
        message.setText("Por favor, haz clic en el siguiente enlace para confirmar tu registro: " + confirmationLink);
        mailSender.send(message);
    }

    public boolean validateConfirmation(String token) {
        // Simulate token validation
        System.out.println("Validating token: " + token);
        // In a real implementation, you would check the token against a database or cache
        return true; // Assume validation is successful for now
    }
}
