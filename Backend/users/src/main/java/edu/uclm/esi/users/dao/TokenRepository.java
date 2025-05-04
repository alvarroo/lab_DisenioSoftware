package edu.uclm.esi.users.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uclm.esi.users.model.Token;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);
}