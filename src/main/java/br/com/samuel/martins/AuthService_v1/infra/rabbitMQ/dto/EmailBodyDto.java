package br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.dto;

public record EmailBodyDto(String id,
                           String username,
                           String sendTo,
                           String subject,
                           String text    ) {
}
