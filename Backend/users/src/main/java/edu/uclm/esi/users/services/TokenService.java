package edu.uclm.esi.users.services;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
        // Crear un token simple (en un entorno real usaríamos JWT)
        JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("timestamp", System.currentTimeMillis());
        
        String token = Base64.getEncoder().encodeToString(json.toString().getBytes());
        tokens.put(token, user.getUsername());
        
        return token;
    }
    
    public boolean validateToken(String token) {
        
        User user = userRepository.findBySesionToken(token);
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