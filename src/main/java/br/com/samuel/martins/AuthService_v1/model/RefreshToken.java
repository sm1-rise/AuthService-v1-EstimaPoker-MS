package br.com.samuel.martins.AuthService_v1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(unique = true)
    @NotBlank
    private String userId;

    @NotBlank
    private String token;

    @NotNull
    private LocalDateTime expiryDate;
}
