package edu.uclm.esi.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.users.dao.UserRepository;
import edu.uclm.esi.users.model.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private EmailService emailService;

    public User register(String username, String password, String email) {
        // Validar que los campos no contengan caracteres maliciosos para prevenir inyecciones
        if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario contiene caracteres no permitidos");
        }
        if (!email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El formato del email no es válido");
        }

        if (username == null || password == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los campos de usuario, contraseña y email son obligatorios");
        }
        
        if (userRepository.findByUsername(username) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        }
        
        User user = new User(username, password, email);
        userRepository.save(user);

        // Generate a confirmation link (placeholder logic)
        String confirmationLink = "http://localhost:8081/email/activate?token=";
        emailService.sendConfirmationEmail(email, confirmationLink, user);

        return user;
    }
    
    public String login(String username, String password) {
        // Limitar intentos de inicio de sesión para prevenir ataques de fuerza bruta
        // (Placeholder para lógica de conteo de intentos fallidos)
        User user = userRepository.findByUsername(username);
        
        if (user == null || !user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        if (!user.isHasActivated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no ha sido activado");
        }
        
        return tokenService.generateToken(user);
    }
    
    public void updatePaymentStatus(String username, boolean hasPaid) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        
        user.setHasPaid(hasPaid);
        userRepository.save(user);
    }
}