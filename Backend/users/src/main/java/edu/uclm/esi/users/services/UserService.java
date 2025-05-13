package edu.uclm.esi.users.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void validatePassword(String password) {
    if (password.length() < 8) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe tener al menos 8 caracteres");
    }

    // Verificar mayúsculas (al menos una, pero no solo al inicio si es la única)
    long uppercaseCount = password.chars().filter(Character::isUpperCase).count();
    if (uppercaseCount == 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe incluir al menos una letra mayúscula");
    }
    if (uppercaseCount == 1 && Character.isUpperCase(password.charAt(0))) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La letra mayúscula no puede estar solo al inicio si es la única");
    }

    // Verificar números
        if (!password.matches(".*[0-9].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe contener al menos un número");
        }

        // Verificar caracteres especiales (al menos uno, pero no solo al final si es el único)
        long specialCharCount = password.chars().filter(c -> "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\~`".indexOf(c) >= 0).count();
        if (specialCharCount == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña debe incluir al menos un carácter especial (#, -, _, @)");
        }
        if (specialCharCount == 1 && "!@#$%^&*()-_=+[]{}|;:'\",.<>?/\\~`".indexOf(password.charAt(password.length() - 1)) >= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carácter especial no puede estar solo al final si es el único");
        }
    }

    public User register(String username, String password, String email) {
        if (username == null || password == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los campos de usuario, contraseña y email son obligatorios");
        }
        
        if (userRepository.findByUsername(username) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        }
        
        // Validar contraseña
        validatePassword(password);
        
        // Cifrar la contraseña
        String encryptedPassword = passwordEncoder.encode(password);
        
        User user = new User(username, encryptedPassword, email);
        userRepository.save(user);

        String confirmationLink = "http://localhost:8081/email/activate?token=";
        emailService.sendConfirmationEmail(email, confirmationLink, user);

        return user;
    }
    
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        if (!user.isHasActivated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario no ha sido activado");
        }
        
        String token = tokenService.generateToken(user);
        user.setSesionToken(token);
        userRepository.save(user);
        return token;
    }
    

    public void updateSesionToken(String token) {
        User user = userRepository.findByUsername(token);
        
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        
        user.setSesionToken(null);;
        userRepository.save(user);
    }

    public String searchBySesionToken(String token) {
        User user = userRepository.findBySesionToken(token);
        
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        
        return user.getUsername();
    }
}