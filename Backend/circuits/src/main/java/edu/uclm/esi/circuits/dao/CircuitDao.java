package edu.uclm.esi.circuits.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.uclm.esi.circuits.model.Circuit;

public interface CircuitDao extends JpaRepository<Circuit, String> {
    List<Circuit> findByUsername(String username);
}
