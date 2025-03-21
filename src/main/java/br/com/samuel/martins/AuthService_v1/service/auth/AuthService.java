package br.com.samuel.martins.AuthService_v1.service.auth;

import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.EmailSender;
import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.dto.EmailPinDto;
import br.com.samuel.martins.AuthService_v1.model.EmailVerification;
import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.ResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.*;
import br.com.samuel.martins.AuthService_v1.repository.EmailVerificationRepository;
import br.com.samuel.martins.AuthService_v1.shared.exception.CustomException;
import br.com.samuel.martins.AuthService_v1.shared.UtilShare;
import br.com.samuel.martins.AuthService_v1.infra.security.token.TokenService;
import static br.com.samuel.martins.AuthService_v1.shared.UtilShare.*;

import br.com.samuel.martins.AuthService_v1.model.User;
import br.com.samuel.martins.AuthService_v1.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AuthService implements AuthServiceUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailSender brokenEmailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, EmailSender brokenEmailSender, EmailVerificationRepository emailVerificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.brokenEmailSender = brokenEmailSender;
    }

    @Override
    @Transactional
    public ResponseDto registerUser(RequestRegisterUserDto request) throws JsonProcessingException {
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
            brokenEmailSender.sendEmail(newUser);

        }else{
            log.warn("Trying to register an existing email: {}", request.email());
            throw new CustomException(400, "Email in use.");
        }
        String token = tokenService.generateToken(newUser);
        log.info("Successfully generated token to user: {}", request.username());
        return new ResponseDto(newUser.getUsername(), token);
    }

    @Override
    //@RateLimiter(name = "SERVICE_NAME", fallbackMethod = "rateLimiterFallback")
    public LoginResponseDto login(UserRequestDto request) {
        log.info("Starting user login: {}", request.email());

        var userFound = Optional.ofNullable(userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(400, WRONG_EMAIL_PASSWORD)));

        if(userFound.isPresent()) {
            var user = userFound.get();
            if(!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
                log.info("Wrong password: {}", request.email());
                throw new CustomException(400, WRONG_EMAIL_PASSWORD);
            }
            log.info("Successfully login: {}", request.email());
            return new LoginResponseDto(user.getId(),
                    user.getUsername(),
                    tokenService.generateToken(user)
            );
        }
        throw  new CustomException(400, WRONG_EMAIL_PASSWORD);
    }


}
