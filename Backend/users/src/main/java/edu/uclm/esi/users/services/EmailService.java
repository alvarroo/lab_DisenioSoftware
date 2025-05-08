package edu.uclm.esi.users.services;
import java.util.UUID;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.users.dao.UserRepository;
import edu.uclm.esi.users.model.User;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    public void sendConfirmationEmail(String email, String confirmationLink, User user) {
        // Validar que el email no sea nulo o vacío
        if (email == null || email.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email no puede estar vacío");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject("Confirmación de Registro");

            String token = generateConfirmationToken();
            confirmationLink = confirmationLink + token;
            user.setActivationToken(token);

            // Formatear el correo con HTML
            String emailContent = "<html><body>" +
                    "<p>Por favor, haz clic en el siguiente botón para confirmar tu registro:</p>" +
                    "<a href='" + confirmationLink + "' style='text-decoration: none;'>" +
                    "<button style='background-color: #4CAF50; color: white; padding: 10px 20px; border: none; cursor: pointer;'>Confirmar Registro</button>" +
                    "</a>" +
                    "</body></html>";

            helper.setText(emailContent, true); // El segundo parámetro indica que es HTML

            mailSender.send(mimeMessage);
            userRepository.save(user); // Guardar el token en la base de datos
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo de confirmación", e);
        }
    }

    public void activate(String token) {

        User user = userRepository.findByActivationToken(token);
        if (user != null) {
            user.setHasActivated(true);
            user.setActivationToken(""); // Limpiar el token después de la activación
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token de activación inválido");
        }
    }
}
