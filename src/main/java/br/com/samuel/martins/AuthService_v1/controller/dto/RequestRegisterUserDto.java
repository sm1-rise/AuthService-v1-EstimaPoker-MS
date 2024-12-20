package br.com.samuel.martins.AuthService_v1.controller.dto;

import br.com.samuel.martins.AuthService_v1.model.Role;

import java.util.Set;

public record RequestRegisterUserDto(String username,
                                     String email,
                                     String password,
                                     Set<Role> role
                                     ) {}
