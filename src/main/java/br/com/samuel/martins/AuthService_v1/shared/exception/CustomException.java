package br.com.samuel.martins.AuthService_v1.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    Integer statusCode;
    String message;
}
