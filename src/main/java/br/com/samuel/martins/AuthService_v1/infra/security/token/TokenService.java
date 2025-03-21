package br.com.samuel.martins.AuthService_v1.infra.security.token;

import br.com.samuel.martins.AuthService_v1.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService implements TokenUseCase{

    @Value("${api.security.token.secret}")
    private String SECRET;


    @Override
    public String generateToken(User user) throws JWTCreationException {
        var algorithm = Algorithm.HMAC256(SECRET);

        var token = JWT.create()
                .withIssuer("authservice")
                .withSubject(user.getEmail())
                .withExpiresAt(this.generateExpirationTime())
                .sign(algorithm);

        return token;
    }

    @Override
    public String validateToken(String token){
        try{
            var algorithm = Algorithm.HMAC256(SECRET);
            return JWT.require(algorithm)
                    .withIssuer("authservice")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant generateExpirationTime() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-3"));
    }

}
