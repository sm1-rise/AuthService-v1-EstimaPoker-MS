package br.com.samuel.martins.AuthService_v1.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<String> getUser(){
        return ResponseEntity.ok("Sucesso!");
    }
}
