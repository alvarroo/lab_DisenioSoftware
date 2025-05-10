package edu.uclm.esi.payments.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @Column(length = 50, name = "NK01WXT")
    private String username;
    
    @Column(length = 70, nullable = false, name = "NK02WXT")
    private String password;
    
    @Column(length = 100, nullable = false, name = "NK03WXT")
    private String email;

    @Column(nullable = false, name = "NK04WXT")
    private boolean hasActivated = false;

    @Column(nullable = false, name = "NK05WXT")
    private boolean hasPaid = false;

    @Column(nullable = false, name = "NK06WXT")
    private int balance = 0;

    @Column(nullable = false, name = "NK07WXT")
    private String activationToken = "";

    @Column(name = "NK08WXT", unique = true)
    private String sesionToken = null;

    public User() {
    }
    
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHasPaid() {
        return hasPaid;
    }

    public void setHasPaid(boolean hasPaid) {
        this.hasPaid = hasPaid;
    }

    public boolean isHasActivated() {
        return hasActivated;
    }

    public void setHasActivated(boolean hasActivated) {
        this.hasActivated = hasActivated;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public String getSesionToken() {
        return sesionToken;
    }

    public void setSesionToken(String sesionToken) {
        this.sesionToken = sesionToken;
    }
}