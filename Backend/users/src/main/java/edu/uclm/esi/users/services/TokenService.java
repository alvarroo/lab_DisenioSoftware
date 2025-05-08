package edu.uclm.esi.users.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uclm.esi.users.dao.UserRepository;
import edu.uclm.esi.users.model.User;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;
    
    private Map<String, String> tokens = new HashMap<>();

    public String generateToken(User user) {
        // Usar un token más seguro con UUID y timestamp
        String token = UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
        tokens.put(token, user.getUsername());
        return token;
    }
    
    public boolean validateToken(String token) {
        // Validar que el token no sea nulo o vacío
        if (token == null || token.isEmpty()) {
            return false;
        }

        String username = tokens.get(token);
        if (username == null) {
            return false;
        }
        
        User user = userRepository.findByUsername(username);
        return user != null && user.isHasPaid();
    }
    
    public User getUserFromToken(String token) {
        String username = tokens.get(token);
        if (username == null) {
            return null;
        }
        
        return userRepository.findByUsername(username);
    }
}