package br.com.samuel.martins.AuthService_v1.repository;

import br.com.samuel.martins.AuthService_v1.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository  extends JpaRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmail(String email);
}
