package edu.uclm.esi.circuits.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.uclm.esi.circuits.model.Circuit;

public interface CircuitDao extends JpaRepository<Circuit, String> {

}
