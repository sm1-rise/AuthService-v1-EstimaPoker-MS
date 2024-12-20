package br.com.samuel.martins.AuthService_v1.shared.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDto> handleCustomException(final CustomException e) {
        return ResponseEntity.status(e.statusCode).body(new ErrorDto(e.statusCode, e.getMessage()));
    }
}
