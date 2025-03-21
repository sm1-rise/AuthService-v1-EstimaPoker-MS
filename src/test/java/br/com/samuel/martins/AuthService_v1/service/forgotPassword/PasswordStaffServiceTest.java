package br.com.samuel.martins.AuthService_v1.service.forgotPassword;

import br.com.samuel.martins.AuthService_v1.shared.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@SpringBootTest
class PasswordStaffServiceTest {


    @Autowired
   private JwtTokenUtil jwtTokenUtil;


    @Test
    void validPin2() throws NoSuchAlgorithmException {
        var token = jwtTokenUtil.generateToken("email@email.com", 5);
        System.out.println(token);


    }

    @Test
    void validPin() throws NoSuchAlgorithmException {

        var pinGenerated = pinGenerator();

        var hashPin = hashPin(pinGenerated);

        System.out.println(hashPin);
        System.out.println(pinGenerated);

        var pinUser = pinGenerated;

        var hashUserPin = hashPin(pinUser);

        Assertions.assertEquals(hashPin, hashUserPin);

        System.out.println(hashUserPin);
        System.out.println(hashPin);

    }

//    private String createJwtToken(String email, String pin) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("email", email);
//        claims.put("pin", pin); // O PIN gerado é armazenado no JWT
//        claims.put("exp", new Date(System.currentTimeMillis() + 5 * 60 * 1000)); // Expiração em 5 minutos
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(email)
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Assina o JWT com a chave secreta
//                .compact();
//    }



//    public String generateToken(String email, int expiration){
//        System.out.println(SECRET);
//        return Jwts.builder()
//                .setSubject(email)
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(SignatureAlgorithm.HS256, SECRET)
//                .compact();
//    }

    private String pinGenerator() {
        var random = new SecureRandom();
        return String.valueOf(random.nextInt(100_000, 999_999));
    }

    private  static  String hashPin(String pin) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(pin.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }



}
