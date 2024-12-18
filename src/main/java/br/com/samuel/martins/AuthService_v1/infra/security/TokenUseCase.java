package br.com.samuel.martins.AuthService_v1.infra.security;

import br.com.samuel.martins.AuthService_v1.model.User;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.time.Instant;

public interface TokenUseCase {
    String generateToken(User user) throws JWTCreationException;

    String validateToken(String token);


}
