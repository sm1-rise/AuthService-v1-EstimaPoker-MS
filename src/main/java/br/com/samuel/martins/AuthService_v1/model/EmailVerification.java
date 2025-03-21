package br.com.samuel.martins.AuthService_v1.model;


import jakarta.persistence.*;
import lombok.*;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    @Column(nullable = false, length = 6)
    private String pin;
    private LocalDateTime expires_at;
    private Boolean isValid;

}


