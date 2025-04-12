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

    public User register(String username, String password, String email) {
        if (username == null || password == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los campos de usuario, contraseña y email son obligatorios");
        }
        
        if (userRepository.findByUsername(username) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El nombre de usuario ya existe");
        }
        
        User user = new User(username, password, email);
        return userRepository.save(user);
    }
    
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        
        if (user == null || !user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
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