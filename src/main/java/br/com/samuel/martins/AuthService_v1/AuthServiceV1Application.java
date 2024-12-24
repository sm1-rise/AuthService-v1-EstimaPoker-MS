package br.com.samuel.martins.AuthService_v1;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class AuthServiceV1Application {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceV1Application.class, args);
	}

}
