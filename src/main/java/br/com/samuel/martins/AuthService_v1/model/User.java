package br.com.samuel.martins.AuthService_v1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Entity
public class User {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotNull @Size(min = 3, max = 30) String getUsername() {
        return username;
    }

    public void setUsername(@NotNull @Size(min = 3, max = 30) String username) {
        this.username = username;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public @NotNull LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    @NotNull
    @Size(min = 3, max = 30)
    private String username;

    @Column(unique = true)
    @NotNull
    private String email;

    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean active;

}
