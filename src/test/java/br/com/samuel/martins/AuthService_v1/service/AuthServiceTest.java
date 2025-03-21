package br.com.samuel.martins.AuthService_v1.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.token.KeyBasedPersistenceTokenService;
import org.springframework.security.core.token.SecureRandomFactoryBean;
import org.springframework.security.core.token.Token;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void createToken() throws Exception {
        var service = new KeyBasedPersistenceTokenService();
        service.setServerSecret("SECRET123");
        service.setServerInteger(16);
        service.setSecureRandom(new SecureRandomFactoryBean().getObject());

        var token = service.allocateToken("user@email.com");

        System.out.println(token.getExtendedInformation());
        System.out.println(new Date(token.getKeyCreationTime()));
        System.out.println(token.getKey());

        //user@email.com
        //Sat Jan 11 23:22:33 BRT 2025
        //MTczNjY0ODU1Mzk2MDo4MmI0YTUzN2JkMDQ4NDdhOWQ3NjNmYWI2ZjdhOGI1ODdhNDFlOWUwZWY5YmFhOGMxNGEyNzYyMDkxZGU4YjVhOnVzZXJAZW1haWwuY29tOmNjZWY2NTlmNDg1ZGRhNzFhMzE2MzAyNDI2MDk3NGE0ZjhjZTg0YTQ0ZjYzYTE1OWZkMjEyNWU5NjE5M2Q5YTI3MDQ5MjQzODkxOTIwOGMxOGU1M2U2ODc2NzNhMTk0NTI0M2Q0NGYxYTdmMWUxMWY5MDgyNWRhZTk1ZmUzMjE5

    }

    @Test
    public void readToken() throws Exception {
        var service = new KeyBasedPersistenceTokenService();
        service.setServerSecret("SECRET123");
        service.setServerInteger(16);
        service.setSecureRandom(new SecureRandomFactoryBean().getObject());

        String rawToken= "MTczNjY0ODU1Mzk2MDo4MmI0YTUzN2JkMDQ4NDdhOWQ3NjNmYWI2ZjdhOGI1ODdhNDFlOWUwZWY5YmFhOGMxNGEyNzYyMDkxZGU4YjVhOnVzZXJAZW1haWwuY29tOmNjZWY2NTlmNDg1ZGRhNzFhMzE2MzAyNDI2MDk3NGE0ZjhjZTg0YTQ0ZjYzYTE1OWZkMjEyNWU5NjE5M2Q5YTI3MDQ5MjQzODkxOTIwOGMxOGU1M2U2ODc2NzNhMTk0NTI0M2Q0NGYxYTdmMWUxMWY5MDgyNWRhZTk1ZmUzMjE5";

        var token = service.verifyToken(rawToken);

        System.out.println(token.getExtendedInformation());
        System.out.println(new Date(token.getKeyCreationTime()));
        System.out.println(token.getKey());
    }


    @Test
    public void readPublicTokenInfo() {
        var rawTokenDecoded = getString();

        var tokenParts = rawTokenDecoded.split(":");

        var timesStamp = Long.parseLong(tokenParts[0]);

        System.out.println(new Date(timesStamp));

        System.out.println(tokenParts[2]);


    }

    private static String getString() {
        var rawToken = "MTczNjY0ODU1Mzk2MDo4MmI0YTUzN2JkMDQ4NDdhOWQ3NjNmYWI2ZjdhOGI1ODdhNDFlOWUwZWY5YmFhOGMxNGEyNzYyMDkxZGU4YjVhOnVzZXJAZW1haWwuY29tOmNjZWY2NTlmNDg1ZGRhNzFhMzE2MzAyNDI2MDk3NGE0ZjhjZTg0YTQ0ZjYzYTE1OWZkMjEyNWU5NjE5M2Q5YTI3MDQ5MjQzODkxOTIwOGMxOGU1M2U2ODc2NzNhMTk0NTI0M2Q0NGYxYTdmMWUxMWY5MDgyNWRhZTk1ZmUzMjE5";

        byte[] bytes = Base64.getDecoder().decode(rawToken);
        var rawTokenDecoded = new String(bytes);
        System.out.println(rawTokenDecoded);
        return rawTokenDecoded;
    }
}
