package com.paperquant.springbackend.services;

import com.paperquant.springbackend.dto.LoginRequest;
import com.paperquant.springbackend.dto.SignupRequest;
import com.paperquant.springbackend.exceptions.PaperQuantException;
import com.paperquant.springbackend.models.NotificationEmail;
import com.paperquant.springbackend.models.User;
import com.paperquant.springbackend.models.VerificationToken;
import com.paperquant.springbackend.repositories.UserRepository;
import com.paperquant.springbackend.repositories.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setCreatedAt(Instant.now());
        user.setEmailIsVerified(false);
        user.setIsAdmin(false);
        userRepository.save(user);

        String verificationToken = generateVerificationToken(user);
        emailService.sendEmail(new NotificationEmail(
                "Please activate your PaperQuant account",
                user.getEmail(),
                "Thank you for signing up with PaperQuant. Please click on the link below to activate your account: "
                        + "http://localhost:8080/api/auth/verify/" + verificationToken));
    }

    @Transactional
    public void login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                                loginRequest.getUsername(),
                                                loginRequest.getPassword()));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) throws PaperQuantException {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new PaperQuantException("Invalid token"));
        fetchUserAndVerify(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndVerify(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new PaperQuantException("User does not exist"));
        user.setEmailIsVerified(true);
        userRepository.save(user);
    }
}
