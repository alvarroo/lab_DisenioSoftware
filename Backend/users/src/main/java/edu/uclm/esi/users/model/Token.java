package edu.uclm.esi.users.model;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "tokens")
public class Token {
    
    @Id
    @Column(length = 36)
    private String id;
    
    @Column(nullable = false, unique = true)
    private String token;
    
    @ManyToOne
    private User user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;
    
    public Token() {
        this.id = UUID.randomUUID().toString();
        this.token = UUID.randomUUID().toString();
        this.creationDate = new Date();
        // Token válido por 24 horas
        this.expirationDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
    }
    
    public Token(User user) {
        this();
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    public boolean isValid() {
        return new Date().before(this.expirationDate);
    }
}