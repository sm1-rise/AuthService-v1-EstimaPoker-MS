package br.com.samuel.martins.AuthService_v1.infra.security.token;

import br.com.samuel.martins.AuthService_v1.model.User;
import com.auth0.jwt.exceptions.JWTCreationException;

public interface TokenUseCase {
    String generateToken(User user) throws JWTCreationException;
    String validateToken(String token);
}
