package com.mrkotuk.PersoNet.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.model.VerificationToken;
import com.mrkotuk.PersoNet.repo.UserRepo;
import com.mrkotuk.PersoNet.repo.VerificaionTokenRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final VerificaionTokenRepo tokenRepo;
    private final AuthenticationManager authManager;
    private final EmailService emailService;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public String register(User user) {
        if (!repo.findByEmail(user.getEmail()).isPresent()) {
            user.setVerified(false);
            user.setPassword(encoder.encode(user.getPassword()));

            repo.save(user);

            return sendVerificationEmail(user);
        } else
            return "Email has already been used!";
    }

    public String verify(User user) {
        if (!repo.findByEmail(user.getEmail()).get().isVerified())
            return sendVerificationEmail(repo.findByEmail(user.getEmail()).get());

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtService.generateToken(user.getEmail());
        } else
            return "Invalid credentials";
    }

    private String sendVerificationEmail(User user) {
        VerificationToken token = new VerificationToken(UUID.randomUUID().toString(), user);
        repo.save(user);
        tokenRepo.save(token);
        emailService.sendVerificatonEmail(user.getEmail(), token.getToken());

        return "Please verify email. Open verification email, and click on the link.";
    }

    public String isVerified(String token) {
        VerificationToken verificationToken = tokenRepo.findByToken(token).get();

        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            tokenRepo.delete(verificationToken);
            return null;
        }

        User user = verificationToken.getUser();
        user.setVerified(true);

        tokenRepo.delete(verificationToken);
        repo.save(user);

        return jwtService.generateToken(verificationToken.getUser().getEmail());
    }

    public User getUserByEmail(String email) {
        return repo.findByEmail(email).get();
    }
}