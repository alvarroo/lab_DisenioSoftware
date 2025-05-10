package edu.uclm.esi.payments.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.payments.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findBySesionToken(String sesionToken);
    
}