package br.com.samuel.martins.AuthService_v1.service;

import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.EmailSender;
import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.dto.EmailBodyDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.RequestRegisterUserDto;
import br.com.samuel.martins.AuthService_v1.model.dto.registerUser.ResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResetPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
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

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class AuthService implements AuthServiceUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailSender brokenEmailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, EmailSender brokenEmailSender) {
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
        var userFound = userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(400, "Email or password doesn't match"));

        if(!passwordEncoder.matches(request.password(), userFound.getPasswordHash())) {
            log.info("Wrong password: {}", request.email());
            throw new CustomException(400, PASSWORD_MESSAGE);
        }

        userFound.setPasswordHash(null);
        log.info("Successfully login: {}", request.email());
        return new LoginResponseDto(userFound.getId(),
                    userFound.getUsername(),
                    tokenService.generateToken(userFound)
                    );
    }

    @Override
    public ResponseResetPasswordDto changePassword(NewPasswordDto request) {
        log.info("Starting change password: {}", request.email());

        var userFound = userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(400, "Email or password doesn't match"));

        if(!passwordEncoder.matches(request.oldPassword(), userFound.getPasswordHash())) {
            log.info("Wrong password: {}", request.email());
            throw new CustomException(400, PASSWORD_MESSAGE);
        }
        userFound.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        log.info("Successfully set new Password: {}", request.email());
        return new ResponseResetPasswordDto("Successfully set new password");
    }

    @Override
    public void sendPINtoEmail(ResetPasswordDto request) {
        Optional<User> userFound = userRepository.findByEmail(request.email());

        if(userFound.isPresent()) {
            //SERVIÇO EMAIL (userFound.get().getEmail)
        } else{
           throw  new CustomException(400, "Email doesn't match");
        }
    }

    @Override
    public void vefifyPIN(ResetPasswordDto request) {

    }

    @Override
    public ResponseResetPasswordDto resetPassword(NewPasswordDto request) {
        return null;
    }

    private LoginResponseDto rateLimiterFallback(UserRequestDto request, Exception ex) {
        throw new CustomException(429, "Too many requests. Please try again later.");
    }
}
