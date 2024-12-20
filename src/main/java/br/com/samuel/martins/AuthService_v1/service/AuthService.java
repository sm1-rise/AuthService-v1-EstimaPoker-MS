package br.com.samuel.martins.AuthService_v1.service;

import br.com.samuel.martins.AuthService_v1.shared.exception.CustomException;
import br.com.samuel.martins.AuthService_v1.shared.UtilShare;
import br.com.samuel.martins.AuthService_v1.controller.dto.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.ResponseDto;
import br.com.samuel.martins.AuthService_v1.controller.dto.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.infra.security.token.TokenService;
import static br.com.samuel.martins.AuthService_v1.shared.UtilShare.*;

import br.com.samuel.martins.AuthService_v1.model.User;
import br.com.samuel.martins.AuthService_v1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AuthService implements AuthServiceUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Override
    public ResponseDto registerUser(RequestRegisterUserDto request) {
        log.info("Starting user register: {}", request.email());

        Optional<User> user = userRepository.findByEmail(request.email());
        User newUser = new User();

        if(user.isEmpty()) {
            log.debug("User not found with this email: {}. register...", request.email());
            newUser.setUsername(request.username());

            if(request.email().matches(UtilShare.REGEX_EMAIL)) {
                log.debug("Valid Email!: {}", request.email());
                newUser.setEmail(request.email());
            } else {
                log.warn("Invalid Email: {}", request.email());
                throw new CustomException(400, EMAIL_MESSAGE);
            }

            if(request.password().matches(UtilShare.REGEX_PASSWORD)) {
                log.debug("Valid passoword to email: {}", request.email());
                newUser.setPasswordHash(passwordEncoder.encode(request.password()));
            }else {
                log.warn("Invalid Password: {}", request.email());
                throw new CustomException(401, PASSWORD_MESSAGE);
            }
            newUser.setRoles(request.role());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setActive(true);
            userRepository.save(newUser);
        }else{
            log.warn("Trying to register an existing email: {}", request.email());
            throw new CustomException(400, "Email in use.");
        }
        String token = tokenService.generateToken(newUser);
        log.info("Successfully generated token to user: {}", request.username());
        return new ResponseDto(newUser.getUsername(), token);
    }

    @Override
    @RateLimiter()
    public LoginResponseDto login(UserRequestDto request) {
        log.info("Starting user login: {}", request.email());
        var userFound = userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(400, "Email or password doesn't match"));

        if(!passwordEncoder.matches(request.password(), userFound.getPasswordHash())) {
            log.info("Wrong password: {}", request.email());
            throw new CustomException(400, PASSWORD_MESSAGE);
        }
        log.info("Successfully login: {}", request.email());
        return new LoginResponseDto(userFound.getId(),
                    userFound.getUsername(),
                    tokenService.generateToken(userFound)
                    );
    }

    public LoginResponseDto rateLimiterFallback(UserRequestDto request, Exception ex) {
        throw new CustomException(429, "Too many requests. Please try again later.");
    }
}
