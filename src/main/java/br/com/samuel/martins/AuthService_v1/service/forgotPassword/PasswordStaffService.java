package br.com.samuel.martins.AuthService_v1.service.forgotPassword;

import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.EmailSender;
import br.com.samuel.martins.AuthService_v1.infra.rabbitMQ.dto.EmailPinDto;
import br.com.samuel.martins.AuthService_v1.infra.security.token.TokenService;
import br.com.samuel.martins.AuthService_v1.model.EmailVerification;
import br.com.samuel.martins.AuthService_v1.model.User;
import br.com.samuel.martins.AuthService_v1.model.dto.login.LoginResponseDto;
import br.com.samuel.martins.AuthService_v1.model.dto.login.UserRequestDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.EmailRequest;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.NewPasswordDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.PinDto;
import br.com.samuel.martins.AuthService_v1.model.dto.reset.ResponseResetPasswordDto;
import br.com.samuel.martins.AuthService_v1.repository.EmailVerificationRepository;
import br.com.samuel.martins.AuthService_v1.repository.UserRepository;
import br.com.samuel.martins.AuthService_v1.shared.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import static br.com.samuel.martins.AuthService_v1.shared.UtilShare.PASSWORD_MESSAGE;

@Service
@Slf4j
public class PasswordStaffService implements PasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailSender brokenEmailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    public PasswordStaffService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, EmailSender brokenEmailSender, EmailVerificationRepository emailVerificationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.brokenEmailSender = brokenEmailSender;
        this.emailVerificationRepository = emailVerificationRepository;
    }

    @Override
    public ResponseResetPasswordDto changePassword(NewPasswordDto request) throws JsonProcessingException {
        log.info("Starting change password: {}", request.email());

        var userFound = Optional.ofNullable(userRepository.findByEmail(request.email()).orElseThrow(() -> new CustomException(400, "Email or password doesn't match")));
        if(userFound.isPresent()){
            var user = userFound.get();

            if(!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
                log.info("Wrong password: {}", request.email());
                throw new CustomException(400, PASSWORD_MESSAGE);
            }

            user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
            brokenEmailSender.sendAlertPasswordChange(user);
            log.info("Successfully set new Password: {}", request.email());
        }
        return new ResponseResetPasswordDto("Successfully set new password");
    }

    public Boolean vefifyPIN(String email, PinDto pin) {
        Optional<EmailVerification> emailVerification = emailVerificationRepository.findByEmail(email);

        if (emailVerification.isPresent()) {
            var verification = emailVerification.get();
            if(verification.getPin().equals(pin.pin())) {
                if(verification.getExpires_at().isAfter(LocalDateTime.now()) && verification.getIsValid()){
                    verification.setIsValid(false);
                    return true;
                }
                else{
                    throw new CustomException(400, "Invalid pin");}
            }
        }
        return false;
    }

    @Override
    public ResponseResetPasswordDto resetPassword(NewPasswordDto request) {
        return null;
    }

    @Override
    public ResponseResetPasswordDto resetPasswordEmail(EmailRequest request) throws NoSuchAlgorithmException, JsonProcessingException {

        Optional<User> userFound = Optional.ofNullable(userRepository.findByEmail(request.email()).orElseThrow(() -> new RuntimeException("Email not found")));

        if(userFound.isPresent()){
            var pinGenerated = pinGenerator();

            var emailPin = EmailVerification.builder()
                    .email(userFound.get().getEmail())
                    .pin(hashPin(pinGenerated))
                    .expires_at(LocalDateTime.now().plusMinutes(5))
                    .isValid(true).build();

            emailVerificationRepository.save(emailPin);
            brokenEmailSender.SendPin(new EmailPinDto(userFound.get().getEmail(), emailPin.getPin()));

            return new ResponseResetPasswordDto("Sucess");
        }

        throw new RuntimeException("Email not found");
    }

    private String pinGenerator() {
        var random = new SecureRandom();
        return String.valueOf(random.nextInt(100_000, 999_999));
    }

    private  static  String hashPin(String pin) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(pin.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    private LoginResponseDto rateLimiterFallback(UserRequestDto request, Exception ex) {
        throw new CustomException(429, "Too many requests. Please try again later.");
    }

    public void metodo(){
        // if(vefifyPIN(emailPin.getEmail(), new PinDto(emailPin.getPin()))){
        //     new ResponseResetPasswordDto("Sucess");
        // }

    }
}
